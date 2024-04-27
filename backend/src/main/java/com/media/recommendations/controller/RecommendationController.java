package com.media.recommendations.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.media.recommendations.model.requests.RecentRecommendationsRequest;
import com.media.recommendations.model.requests.RecommendationRatingRequest;
import com.media.recommendations.model.requests.RecommendationRequest;
import com.media.recommendations.model.responses.RecommendationResponse;
import com.media.recommendations.service.MovieService;
import com.media.recommendations.service.RecommendationService;
import com.media.recommendations.service.SongService;

import lombok.AllArgsConstructor;


@AllArgsConstructor
@CrossOrigin
@RestController
@RequestMapping("/api/v1/recommend")
public class RecommendationController {

    RecommendationService recommendationService;
    MovieService movieService;
    SongService songService;

    @PostMapping
    public ResponseEntity<RecommendationResponse> getRecommendation(@RequestBody RecommendationRequest request) {
        RecommendationResponse response = recommendationService.getRecommendation(request);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PostMapping("/neural")
    public ResponseEntity<RecommendationResponse> getNeuralModelRecommendation(@RequestBody RecommendationRequest request) {
        RecommendationResponse result = recommendationService.getModelRecommendation(request);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @PostMapping("/rate")
    public ResponseEntity<RecommendationResponse> rateRecommendation(@RequestBody RecommendationRatingRequest request) {
        recommendationService.rateRecommendation(request);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PostMapping("/recent")
    public ResponseEntity<List<RecommendationResponse>> getRecentRecommendations(@RequestBody RecentRecommendationsRequest request) {
        List<RecommendationResponse> results = recommendationService.getRecentRecommendations(request.getUsername());
        return new ResponseEntity<>(results, HttpStatus.OK);
    }
}
