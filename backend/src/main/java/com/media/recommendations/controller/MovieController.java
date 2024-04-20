package com.media.recommendations.controller;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;

import java.util.ArrayList;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.media.recommendations.model.Movie;
import com.media.recommendations.model.requests.GetClosestMovieRequest;
import com.media.recommendations.model.requests.MovieSearchRequest;
import com.media.recommendations.model.requests.OmdbRequest;
import com.media.recommendations.model.requests.TempPairsRequest;
import com.media.recommendations.model.responses.MoviePageResponse;
import com.media.recommendations.model.responses.TempPairsResponse;
import com.media.recommendations.service.MovieService;


@AllArgsConstructor
@CrossOrigin
@RestController
@RequestMapping("/api/v1/movies")
public class MovieController {
    MovieService movieService;

    @GetMapping
    public ResponseEntity<List<Movie>> getAllMovies() {
        return new ResponseEntity<>(movieService.getAllMovies(), HttpStatus.OK);
    }

    @GetMapping("/page")
    public ResponseEntity<MoviePageResponse> getPageMovies(@RequestParam(defaultValue = "0") Integer page,
            @RequestParam(defaultValue = "10") Integer size) {
        return new ResponseEntity<>(movieService.getPageMovies(page, size), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Movie> getMovieById(@PathVariable long id) {
        if (movieService.existsMovie(id)) {
            return new ResponseEntity<>(movieService.getMovieById(id), HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @PostMapping("/omdb")
    public ResponseEntity<Movie> getMovieFromOmdb(@Valid @RequestBody OmdbRequest request) {
        Movie movie = movieService.getMovieFromOmdb(request.getTitle());
        if (movie != null) {
            return new ResponseEntity<>(movie, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @PostMapping
    public ResponseEntity<Movie> createMovie(@Valid @RequestBody Movie movie) {
        return new ResponseEntity<>(movieService.createMovie(movie), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Movie> updateMovie(@PathVariable long id, @Valid @RequestBody Movie movie) {
        if (movieService.existsMovie(id)) {
            return new ResponseEntity<>(movieService.updateMovie(id, movie), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping({"/{id}"})
    public ResponseEntity<Movie> deleteMovie(@PathVariable Long id) {
        if (movieService.existsMovie(id)) {
            movieService.deleteMovie(id);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @PostMapping("/search")
    public ResponseEntity<MoviePageResponse> searchMovies(@RequestBody @Valid MovieSearchRequest request) {
        MoviePageResponse movies = movieService.search(request.getSearch());
        return new ResponseEntity<>(movies, HttpStatus.OK);
    }

    // @PostMapping("/search-closest")
    // public ResponseEntity<String> searchClosestMovieFeatures(@RequestBody GetClosestMovieRequest request) {
    //     String response = movieService.getClosestMovieFromFeatures(request.getGenres(), request.getYear(), request.getRuntime());
    //     return new ResponseEntity<>(response, HttpStatus.OK);
    // }
    
    @PostMapping("/testimdbid")
    public ResponseEntity<Movie> postMethodName(@RequestBody String entity) {
        Movie name = movieService.getMovieFromOmdbByIMBDID(entity);
        return new ResponseEntity<>(name, HttpStatus.OK);
    }

    @PostMapping("/getPairs")
    public ResponseEntity<List<TempPairsResponse>> postMethodName(@RequestBody List<TempPairsRequest> entity) {
        List<TempPairsResponse> result = new ArrayList<>();
        for(TempPairsRequest req : entity) {
            Movie first = movieService.getMovieFromOmdb(req.getFirst());
            Movie second = movieService.getMovieFromOmdb(req.getSecond());
            TempPairsResponse resp = new TempPairsResponse();
            resp.setFirstMovie(first);
            resp.setSecondMovie(second);
            result.add(resp);
        }
    
        return new ResponseEntity<>(result, HttpStatus.OK);
    }
}
