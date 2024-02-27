package com.media.recommendations.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.media.recommendations.model.requests.RecommendationRatingRequest;
import com.media.recommendations.model.requests.RecommendationRequest;
import com.media.recommendations.model.responses.RecommendationResponse;
import com.media.recommendations.service.RecommendationService;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;

@AllArgsConstructor
@CrossOrigin
@RestController
@RequestMapping("/api/v1/recommend")
public class RecommendationController {

    RecommendationService recommendationService;

    @PostMapping
    public ResponseEntity<RecommendationResponse> getRecommendation(@Valid @RequestBody RecommendationRequest request) {
        RecommendationResponse response = recommendationService.getRecommendation(request);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PostMapping("/rate")
    public ResponseEntity<RecommendationResponse> rateRecommendation(@Valid @RequestBody RecommendationRatingRequest request) {
        recommendationService.rateRecommendation(request);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
