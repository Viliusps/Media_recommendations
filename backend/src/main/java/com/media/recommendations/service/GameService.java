package com.media.recommendations.service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.media.recommendations.model.GameFeatures;

@Service
public class GameService {
    @Value("${steam.api.key}")
    private String apiKey;

    @Value("${rawg.api.key}")
    private String rawgApiKey;

    private static final String RAWG_API_BASE_URL = "https://api.rawg.io/api/";

    private final RestTemplate restTemplate;

    public GameService() {
        this.restTemplate = new RestTemplate();
    }

    public ResponseEntity<String> getRecentlyPlayedGames(String userId) {
        String apiUrl = "https://api.steampowered.com/IPlayerService/GetRecentlyPlayedGames/v1/";
            String url = apiUrl + "?key=" + apiKey + "&steamid=" + userId;

            RestTemplate restTemplate = new RestTemplate();
            ResponseEntity<String> response = restTemplate.getForEntity(url, String.class);

            GameFeatures features = getGameFeatures("Red Dead Redemption 2");
            System.out.println(features.toString());

            return response;
    }

    public GameFeatures getGameFeatures(String gameName) {
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

            return parseGameFeaturesFromDetailsResponse(gameDetailsResponse);
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

    private GameFeatures parseGameFeaturesFromDetailsResponse(String detailsResponse) {
        try {
            ObjectMapper mapper = new ObjectMapper();
            JsonNode root = mapper.readTree(detailsResponse);
            GameFeatures gameFeatures = new GameFeatures();
            gameFeatures.setName(root.get("name").asText());
            gameFeatures.setReleaseDate(root.get("released").asText());
            gameFeatures.setGenres(parseGenres(root.get("genres")));
            gameFeatures.setRating(root.get("rating").asDouble());
            gameFeatures.setPlaytime(root.get("playtime").asInt());
            return gameFeatures;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    private List<String> parseGenres(JsonNode genresNode) {
        List<String> genres = new ArrayList<>();
        if (genresNode.isArray()) {
            for (JsonNode genreNode : genresNode) {
                genres.add(genreNode.get("name").asText());
            }
        }
        return genres;
    }


}
