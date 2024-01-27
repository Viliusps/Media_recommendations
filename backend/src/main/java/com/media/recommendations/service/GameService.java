package com.media.recommendations.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class GameService {
    @Value("${steam.api.key}")
    private String apiKey;

    public ResponseEntity<String> getRecentlyPlayedGames() {
        String apiUrl = "https://api.steampowered.com/IPlayerService/GetRecentlyPlayedGames/v1/";
            String url = apiUrl + "?key=" + apiKey + "&steamid=76561198132196957";

            RestTemplate restTemplate = new RestTemplate();
            ResponseEntity<String> response = restTemplate.getForEntity(url, String.class);

            return response;
    }
}
