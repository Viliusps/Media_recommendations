package com.media.recommendations.service;
import java.util.List;

import lombok.AllArgsConstructor;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.media.recommendations.model.Movie;
import com.media.recommendations.repository.MovieRepository;

@AllArgsConstructor
@Service
public class MovieService {
    private final MovieRepository movieRepository;

    public List<Movie> getAllMovies() {
        return movieRepository.findAllByOrderByIdAsc();
    }

    public Page<Movie> getPageMovies(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return movieRepository.findAll(pageable);
    }

     public Movie getMovieById(long id) {
        return movieRepository.findById(id).get();
    }

    public Movie createMovie(Movie movie) {
        Movie newMovie = new Movie();
        newMovie.setAdult(movie.isAdult());
        newMovie.setBelongsToCollection(movie.getBelongsToCollection());
        newMovie.setComments(movie.getComments());
        newMovie.setGenres(movie.getGenres());
        newMovie.setImdbId(movie.getImdbId());
        newMovie.setOriginalLanguage(movie.getOriginalLanguage());
        newMovie.setOriginalTitle(movie.getOriginalTitle());
        newMovie.setOverview(movie.getOverview());
        newMovie.setPopularity(movie.getPopularity());
        newMovie.setProductionCountries(movie.getProductionCountries());
        newMovie.setReleaseDate(movie.getReleaseDate());
        newMovie.setRuntime(movie.getRuntime());
        newMovie.setSpokenLanguages(movie.getSpokenLanguages());
        newMovie.setTitle(movie.getTitle());
        newMovie.setVoteAverage(movie.getVoteAverage());
        newMovie.setVoteCount(movie.getVoteCount());
        return movieRepository.save(newMovie);
    }

    public boolean existsMovie(long id) {
        return movieRepository.existsById(id);
    }

    public Movie updateMovie(Long id, Movie movie) {
        Movie movieFromDb = movieRepository.findById(id).get();
        movieFromDb.setAdult(movie.isAdult());
        movieFromDb.setBelongsToCollection(movie.getBelongsToCollection());
        movieFromDb.setComments(movie.getComments());
        movieFromDb.setGenres(movie.getGenres());
        movieFromDb.setImdbId(movie.getImdbId());
        movieFromDb.setOriginalLanguage(movie.getOriginalLanguage());
        movieFromDb.setOriginalTitle(movie.getOriginalTitle());
        movieFromDb.setOverview(movie.getOverview());
        movieFromDb.setPopularity(movie.getPopularity());
        movieFromDb.setProductionCountries(movie.getProductionCountries());
        movieFromDb.setReleaseDate(movie.getReleaseDate());
        movieFromDb.setRuntime(movie.getRuntime());
        movieFromDb.setSpokenLanguages(movie.getSpokenLanguages());
        movieFromDb.setTitle(movie.getTitle());
        movieFromDb.setVoteAverage(movie.getVoteAverage());
        movieFromDb.setVoteCount(movie.getVoteCount());
        return movieRepository.save(movieFromDb);
    }

    public void deleteMovie(Long id) {
        movieRepository.deleteById(id);
    }
    
}
