package com.media.recommendations.service;

import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.media.recommendations.model.Game;
import com.media.recommendations.model.NeuralModelGameFeatures;
import com.media.recommendations.model.SteamHistory;
import com.media.recommendations.model.User;
import com.media.recommendations.model.responses.GamePageResponse;
import com.media.recommendations.model.responses.SteamHistoryResponse;
import com.media.recommendations.repository.GameRepository;
import com.media.recommendations.repository.SteamRepository;

@Service
public class GameService {
    @Value("${steam.api.key}")
    private String steamApiKey;

    @Value("${rawg.api.key}")
    private String rawgApiKey;

    private static final String RAWG_API_BASE_URL = "https://api.rawg.io/api/";

    private final RestTemplate restTemplate;

    private final GameRepository gameRepository;

    private final SteamRepository steamRepository;

    private final UserService userService;

    public GameService(GameRepository gameRepository, UserService userService, SteamRepository steamRepository) {
        this.restTemplate = new RestTemplate();
        this.gameRepository = gameRepository;
        this.userService = userService;
        this.steamRepository = steamRepository;
    }

    public GamePageResponse getPageGames(int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("popularity").descending());
        Page<Game> gamesPage = gameRepository.findAll(pageable);

        List<Game> games = gamesPage.getContent();
        long totalGames = gameRepository.count();

        return new GamePageResponse(games, totalGames);
    }

    public GamePageResponse search(String search) {
        List<Game> found = gameRepository.findByNameContaining(search);
        GamePageResponse response = new GamePageResponse(found, found.size());
        return response;
    }

    public boolean existsGame(Game game) {
        if (game == null) {
            return false;
        }
        return gameRepository.existsByName(game.getName());
    }

    public boolean existsGame(long id) {
        return gameRepository.existsById(id);
    }

    public Game getByName(String name) {
        return gameRepository.getByName(name);
    }

    public Game createGame(Game game) {
        return gameRepository.save(game);
    }

    public Game getGameById(long id) {
        Optional<Game> optionalGame = gameRepository.findById(id);
        if (optionalGame.isPresent()) {
            return optionalGame.get();
        }
        return null;
    }

    public SteamHistoryResponse getSteamHistory(String username) {
        User user = userService.userByUsername(username);
        Long userId = user.getId();
        List<SteamHistory> history = steamRepository.findAllByUserId(userId);
        if(history.size() > 0) {
            List<Game> games = history.stream()
                    .map(SteamHistory::getGame)
                    .collect(Collectors.toList());
            SteamHistoryResponse response = new SteamHistoryResponse(games, history.get(0).getDate());
            return response;
        }
        return new SteamHistoryResponse();
    }

    public String getSteamHistoryNames(String username) {
        User user = userService.userByUsername(username);
        Long userId = user.getId();
        List<SteamHistory> history = steamRepository.findAllByUserId(userId);
        if(history.size() > 0) {
            List<Game> games = history.stream()
                    .map(SteamHistory::getGame)
                    .collect(Collectors.toList());
            String allGameNames = games.stream()
                    .map(Game::getName)
                    .collect(Collectors.joining(", "));
            return allGameNames;
        }
        return "";
    }

    public ResponseEntity<String> getRecentlyPlayedGames(String userId, String username) {
        String apiUrl = "https://api.steampowered.com/IPlayerService/GetRecentlyPlayedGames/v1/";
        String url = apiUrl + "?key=" + steamApiKey + "&steamid=" + userId + "&count=5";

        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<String> response = restTemplate.getForEntity(url, String.class);
        List<Game> games = extractGamesFromSteamResponse(response);
        addGamesToHistory(games, username, userId);
        return response;
    }

    public Game calculateAverage(String username) {
        List<Game> games = getSteamHistory(username).getGames();
        Game averageGame = new Game();

        Map<String, Integer> genreFrequency = new HashMap<>();
        for (Game game : games) {
            genreFrequency.put(game.getGenres(), genreFrequency.getOrDefault(game.getGenres(), 0) + 1);
        }
        String mostFrequentGenre = Collections.max(genreFrequency.entrySet(), Map.Entry.comparingByValue()).getKey();
        averageGame.setGenres(mostFrequentGenre);

        List<LocalDate> dates = games.stream()
            .map(game -> LocalDate.parse(game.getReleaseDate(), DateTimeFormatter.ISO_LOCAL_DATE))
            .sorted()
            .collect(Collectors.toList());
        LocalDate medianDate = dates.get(dates.size() / 2);
        averageGame.setReleaseDate(medianDate.format(DateTimeFormatter.ISO_LOCAL_DATE));

        double averageRating = games.stream()
            .mapToDouble(Game::getRating)
            .average()
            .orElse(0.0);
        averageGame.setRating(averageRating);

        int averagePlaytime = (int) games.stream()
            .mapToInt(Game::getPlaytime)
            .average()
            .orElse(0);
        averageGame.setPlaytime(averagePlaytime);

        return averageGame;
    }

    private void addGamesToHistory(List<Game> games, String username, String steamUserId) { 
        User user = userService.userByUsername(username);
        steamRepository.deleteByUser(user);

        LocalDate currDate = LocalDate.now();
        for(Game game : games) {
            if(!existsGame(game)) {
                game = createGame(game);
            } else {
                game = getByName(game.getName());
            }
            SteamHistory entry = new SteamHistory();
            entry.setDate(currDate);
            entry.setGame(game);
            entry.setUser(user);
            entry.setSteamId(steamUserId);
            steamRepository.save(entry);
        }
    }

    public List<Game> extractGamesFromSteamResponse(ResponseEntity<String> response) {
        List<Game> fullGames = new ArrayList<>();
        ObjectMapper mapper = new ObjectMapper();

        try {
            JsonNode root = mapper.readTree(response.getBody());
            JsonNode games = root.path("response").path("games");

            if (games.isArray()) {
                for (JsonNode game : games) {
                    String name = game.path("name").asText();
                    Game fullGame = getGameFromRAWG(name);
                    fullGames.add(fullGame);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return fullGames;
    }

    public List<Game> getGameSuggestions(String gameName) throws JsonMappingException, JsonProcessingException {
        List<Game> games = new ArrayList<>();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("User-Agent", "MyUniProject/v1.0");

        String encodedGameName = URLEncoder.encode(gameName, StandardCharsets.UTF_8);
        String searchUrl = RAWG_API_BASE_URL + "games?search=" + encodedGameName + "&key=" + rawgApiKey;

        HttpEntity<String> entity = new HttpEntity<>(headers);
        String gameSearchResponse = restTemplate.exchange(searchUrl, HttpMethod.GET, entity, String.class).getBody();
        ObjectMapper mapper = new ObjectMapper();
        JsonNode root = mapper.readTree(gameSearchResponse);
        JsonNode resultsNode = root.get("results");
        for(int i = 0; i < Math.min(resultsNode.size(), 5); i++) {
            Game game = parseRAWGJsonNode(resultsNode.get(i));
            games.add(game);
        }
        return games;
    }

    public Game getGameFromRAWGByID(String gameId) {
        Game gameFromDb = gameRepository.getByrawgID(Integer.parseInt(gameId));
        if(gameFromDb != null) {
            return gameFromDb;
        }
        else {       
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            headers.set("User-Agent", "MyUniProject/v1.0");
            HttpEntity<String> entity = new HttpEntity<>(headers);

            String gameDetailsUrl = RAWG_API_BASE_URL + "games/" + gameId + "?key=" + rawgApiKey;
            String gameDetailsResponse = restTemplate.exchange(gameDetailsUrl, HttpMethod.GET, entity, String.class).getBody();

            return parseGameFromDetailsResponse(gameDetailsResponse);
        }
 
    }

    public Game getGameFromRAWG(String gameName) {
         Game gameFromDb = gameRepository.getByName(gameName);
        if(gameFromDb != null) {
            return gameFromDb;
        }
        else {     
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            headers.set("User-Agent", "MyUniProject/v1.0");

            String encodedGameName = URLEncoder.encode(gameName, StandardCharsets.UTF_8);
            String searchUrl = RAWG_API_BASE_URL + "games?search=" + encodedGameName + "&key=" + rawgApiKey;

            HttpEntity<String> entity = new HttpEntity<>(headers);
            String gameSearchResponse = restTemplate.exchange(searchUrl, HttpMethod.GET, entity, String.class).getBody();

            String gameId = parseGameIdFromSearchResponse(gameSearchResponse);

            if (gameId != null) {
                return getGameFromRAWGByID(gameId);
            } else {
                return null;
            }
        }
    }

    private Game parseRAWGJsonNode(JsonNode node) {
        Game game = new Game();
        game.setName(node.get("name").asText());
        game.setReleaseDate(node.get("released").asText());
        ArrayNode genres = (ArrayNode) node.get("genres");
        String genresStr = StreamSupport.stream(genres.spliterator(), false)
                                        .map(genre -> genre.get("name").asText())
                                        .collect(Collectors.joining(", "));
        game.setGenres(genresStr);

        ArrayNode platforms = (ArrayNode) node.get("platforms");
        String platformsStr = StreamSupport.stream(platforms.spliterator(), false)
                                        .map(platform -> platform.get("platform").get("name").asText())
                                        .collect(Collectors.joining(", "));
        game.setPlatforms(platformsStr);
        if(node.get("description") != null) game.setDescription(node.get("description").asText());
        game.setRating(node.get("rating").asDouble());
        game.setPlaytime(node.get("playtime").asInt());
        game.setBackgroundImage(node.get("background_image").asText());
        game.setRawgID(node.get("id").asInt());
        return game;
    }

    private String parseGameIdFromSearchResponse(String searchResponse) {
        try {
            ObjectMapper mapper = new ObjectMapper();
            JsonNode root = mapper.readTree(searchResponse);
            JsonNode resultsNode = root.get("results");
            if (resultsNode.isArray() && resultsNode.size() > 0) {
                return resultsNode.get(0).get("id").asText();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    private Game parseGameFromDetailsResponse(String detailsResponse) {
        try {
            ObjectMapper mapper = new ObjectMapper();
            JsonNode root = mapper.readTree(detailsResponse);
            Game game = new Game();
            game.setName(root.get("name").asText());
            game.setReleaseDate(root.get("released").asText());
            ArrayNode genres = (ArrayNode) root.get("genres");
            String genresStr = StreamSupport.stream(genres.spliterator(), false)
                                            .map(genre -> genre.get("name").asText())
                                            .collect(Collectors.joining(", "));
            game.setGenres(genresStr);

            ArrayNode platforms = (ArrayNode) root.get("platforms");
            String platformsStr = StreamSupport.stream(platforms.spliterator(), false)
                                            .map(platform -> platform.get("platform").get("name").asText())
                                            .collect(Collectors.joining(", "));
            game.setPlatforms(platformsStr);
            if(root.get("description") != null) game.setDescription(root.get("description").asText());
            game.setRating(root.get("rating").asDouble());
            game.setPlaytime(root.get("playtime").asInt());
            game.setBackgroundImage(root.get("background_image").asText());
            game.setRawgID(root.get("id").asInt());
            if(!gameRepository.existsByName(game.getName())) gameRepository.save(game);
            return game;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    private List<Game> parseGamesFromResponse(String jsonResponse) {
        List<Game> games = new ArrayList<>();
        ObjectMapper mapper = new ObjectMapper();
        try {
            JsonNode root = mapper.readTree(jsonResponse);
            JsonNode results = root.path("results");
            for (JsonNode node : results) {
                Game game = parseGameFromDetailsResponse(node.toString());
                if (game != null) {
                    games.add(game);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return games;
    }

    public Game findGameFromFeatures(NeuralModelGameFeatures features) {
        String genres = features.getGenre();
        int year = features.getYear();
        double minimumRating = features.getRating();
        int playtime = features.getPlaytime();
        String dateRange = year + "-01-01," + year + "-12-31";

        String url = "https://api.rawg.io/api/games?key=" + rawgApiKey + "&genres=" + genres.toLowerCase() + "&dates=" + dateRange;

        ResponseEntity<String> response = restTemplate.getForEntity(url, String.class);

        List<Game> games = parseGamesFromResponse(response.getBody());

         Game closestMatch = games.stream()
                .min(Comparator.comparingDouble(
                        game -> Math.abs(game.getRating() - minimumRating) + Math.abs(game.getPlaytime() - playtime))
                ).orElse(null);
                
        return closestMatch != null ? closestMatch : new Game();

    }

    public void increasePopularity(Game game) {
        Game gameFromDb = getByName(game.getName());
        gameFromDb.setPopularity(gameFromDb.getPopularity() + 1);
        gameRepository.save(gameFromDb);
    }
}
