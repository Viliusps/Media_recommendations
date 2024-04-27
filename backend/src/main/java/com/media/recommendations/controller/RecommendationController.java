package com.media.recommendations.controller;

import java.util.List;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.media.recommendations.model.Movie;
import com.media.recommendations.model.requests.RecentRecommendationsRequest;
import com.media.recommendations.model.requests.RecommendationRatingRequest;
import com.media.recommendations.model.requests.RecommendationRequest;
import com.media.recommendations.model.requests.TempRequest;
import com.media.recommendations.model.responses.RecommendationResponse;
import com.media.recommendations.model.responses.TempResponse;
import com.media.recommendations.service.MovieService;
import com.media.recommendations.service.RecommendationService;
import com.media.recommendations.service.SongService;

import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;


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

    @GetMapping("/testScript")
    public String test() {
        return recommendationService.executePythonScript("Song", "Movie");
    }

    @PostMapping("/temp")
    public ResponseEntity<TempResponse> temp(@RequestBody TempRequest request) {
        Movie movie = movieService.getMovieFromOmdb(request.getName());
        Map<String, String> song = songService.getSongFeaturesByMBID(request.getMbid());
        TempResponse response = new TempResponse();
        response.setMovie(movie);
        response.setSong(song);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
    @PostMapping("/test")
    public ResponseEntity<RecommendationResponse> postMethodName(@RequestBody RecommendationRequest request) {
        RecommendationResponse result = recommendationService.getModelRecommendation(request);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }
}
