package com.media.recommendations.controller;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.media.recommendations.model.RecommendationRequest;
import com.media.recommendations.model.RecommendationResponse;


@AllArgsConstructor
@CrossOrigin
@RestController
@RequestMapping("/api/v1/recommend")
public class RecommendationController {
    @PostMapping
    public ResponseEntity<RecommendationResponse> createSong(@Valid @RequestBody RecommendationRequest request) {
        return new ResponseEntity<>(null, HttpStatus.OK);
    }
}
