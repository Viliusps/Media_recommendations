package com.media.recommendations.controller;

import lombok.AllArgsConstructor;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.media.recommendations.model.Game;
import com.media.recommendations.model.requests.NameRequest;
import com.media.recommendations.model.requests.SteamRequest;
import com.media.recommendations.service.GameService;


@AllArgsConstructor
@CrossOrigin
@RestController
@RequestMapping("/api/v1/games")
public class GameController {
    GameService gameService;

    @PostMapping("/getRecentlyPlayedGames")
    public ResponseEntity<String> getRecentlyPlayedGames(@RequestBody SteamRequest request) {
        return gameService.getRecentlyPlayedGames(request.getUserId());
    }

    @PostMapping("/check")
    public ResponseEntity<Boolean> checkIfGameExists(@RequestBody NameRequest request) {
        Boolean result = gameService.checkIfGameExists(request.getName());
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @PostMapping("/getGameFromFeatures")
    public ResponseEntity<Game> getGameFromFeatures(@RequestBody Game game) {
        Game response = gameService.findGameFromFeatures(game.getGenre(), game.getReleaseDate(), game.getRating(), game.getPlaytime());
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
    
}
