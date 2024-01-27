package com.media.recommendations.controller;

import lombok.AllArgsConstructor;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.media.recommendations.service.GameService;


@AllArgsConstructor
@CrossOrigin
@RestController
@RequestMapping("/api/v1/games")
public class GameController {
    GameService gameService;


    @GetMapping("/getRecentlyPlayedGames")
    public ResponseEntity<String> getRecentlyPlayedGames() {
        return gameService.getRecentlyPlayedGames();
    }
    
}
