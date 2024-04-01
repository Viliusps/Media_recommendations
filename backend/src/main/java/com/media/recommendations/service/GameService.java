package com.media.recommendations.service;

import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

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

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.media.recommendations.model.Game;
import com.media.recommendations.model.responses.GamePageResponse;
import com.media.recommendations.repository.GameRepository;

@Service
public class GameService {
    @Value("${steam.api.key}")
    private String steamApiKey;

    @Value("${rawg.api.key}")
    private String rawgApiKey;

    private static final String RAWG_API_BASE_URL = "https://api.rawg.io/api/";

    private final RestTemplate restTemplate;

    private final GameRepository gameRepository;

    public GameService(GameRepository gameRepository) {
        this.restTemplate = new RestTemplate();
        this.gameRepository = gameRepository;
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

    public Game getByName(String name) {
        return gameRepository.getByName(name);
    }

    public Game createGame(Game game) {
        return gameRepository.save(game);
    }

    public ResponseEntity<String> getRecentlyPlayedGames(String userId) {
            String apiUrl = "https://api.steampowered.com/IPlayerService/GetRecentlyPlayedGames/v1/";
            String url = apiUrl + "?key=" + steamApiKey + "&steamid=" + userId;

            RestTemplate restTemplate = new RestTemplate();
            ResponseEntity<String> response = restTemplate.getForEntity(url, String.class);
            return response;
    }

    public Game getGameFromRAWG(String gameName) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("User-Agent", "MyUniProject/v1.0");

        String searchUrl = RAWG_API_BASE_URL + "games?search=" + gameName + "&key=" + rawgApiKey;

        HttpEntity<String> entity = new HttpEntity<>(headers);
        String gameSearchResponse = restTemplate.exchange(searchUrl, HttpMethod.GET, entity, String.class).getBody();

        String gameId = parseGameIdFromSearchResponse(gameSearchResponse);

        if (gameId != null) {

            String gameDetailsUrl = RAWG_API_BASE_URL + "games/" + gameId + "?key=" + rawgApiKey;

            String gameDetailsResponse = restTemplate.exchange(gameDetailsUrl, HttpMethod.GET, entity, String.class).getBody();

            return parseGameFromDetailsResponse(gameDetailsResponse);
        } else {
            return null;
        }
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
            game.setGenre(root.get("genres").get(0).get("name").asText());
            game.setRating(root.get("rating").asDouble());
            game.setPlaytime(root.get("playtime").asInt());
            game.setBackgroundImage(root.get("background_image").asText());
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


    @SuppressWarnings({ "unchecked", "rawtypes" })
    public boolean checkIfGameExists(String gameName) {
        try {
            String modifiedGameName = gameName.replace(" ", "-").toLowerCase();
            String encodedGameName = URLEncoder.encode(modifiedGameName, StandardCharsets.UTF_8.toString());
            final String url = "https://api.rawg.io/api/games?key=" + rawgApiKey + "&search=" + encodedGameName + "&search_precise=true";
            RestTemplate restTemplate = new RestTemplate();
            ResponseEntity<Map> response = restTemplate.getForEntity(url, Map.class);

            Map<String, Object> responseBody = response.getBody();
            if (responseBody != null && responseBody.containsKey("results")) {
                List<Map<String, Object>> results = (List<Map<String, Object>>) responseBody.get("results");

                for (Map<String, Object> game : results) {
                    String name = (String) game.get("name");
                    if (gameName.equalsIgnoreCase(name)) {
                        return true;
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public Game findGameFromFeatures(String genres, String releaseDate, double minimumRating, int playtime) {
        String year = releaseDate.substring(0, 4);
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
