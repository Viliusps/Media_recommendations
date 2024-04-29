package com.media.recommendations.controller;

import lombok.AllArgsConstructor;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.media.recommendations.model.Game;
import com.media.recommendations.model.requests.GameSearchRequest;
import com.media.recommendations.model.requests.GetSteamHistoryRequest;
import com.media.recommendations.model.requests.NameRequest;
import com.media.recommendations.model.requests.SteamRequest;
import com.media.recommendations.model.responses.GamePageResponse;
import com.media.recommendations.model.responses.SteamHistoryResponse;
import com.media.recommendations.service.GameService;
import jakarta.validation.Valid;


@AllArgsConstructor
@CrossOrigin
@RestController
@RequestMapping("/api/v1/games")
public class GameController {
    GameService gameService;

    @PostMapping("/getRecentlyPlayedGames")
    public ResponseEntity<String> getRecentlyPlayedGames(@RequestBody SteamRequest request) {
        return gameService.getRecentlyPlayedGames(request.getUserId(), request.getUsername());
    }
    
    @PostMapping("/search")
    public ResponseEntity<GamePageResponse> searchGames(@RequestBody @Valid GameSearchRequest request) {
        GamePageResponse games = gameService.search(request.getSearch());
        return new ResponseEntity<>(games, HttpStatus.OK);
    }

    @GetMapping("/page")
    public ResponseEntity<GamePageResponse> getPageGames(@RequestParam(defaultValue = "0") Integer page, @RequestParam(defaultValue = "10") Integer size) {
        return new ResponseEntity<>(gameService.getPageGames(page, size), HttpStatus.OK);
    }

    @PostMapping("/steam-history")
    public ResponseEntity<SteamHistoryResponse> getSteamHistory(@RequestBody GetSteamHistoryRequest request) {
        SteamHistoryResponse response = gameService.getSteamHistory(request.getUsername());
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Game> getGameById(@PathVariable long id) {
        if (gameService.existsGame(id)) {
            return new ResponseEntity<>(gameService.getGameById(id), HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @PostMapping("/suggestions")
    public ResponseEntity<List<Game>> getGameSuggestions(@RequestBody NameRequest request) throws JsonMappingException, JsonProcessingException {
        List<Game> games = gameService.getGameSuggestions(request.getName());
        return new ResponseEntity<>(games, HttpStatus.OK);
    }
}
