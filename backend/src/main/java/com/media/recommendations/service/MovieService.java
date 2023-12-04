package com.media.recommendations.service;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.media.recommendations.model.Movie;
import com.media.recommendations.model.MoviePageResponse;
import com.media.recommendations.model.OmdbMovie;
import com.media.recommendations.repository.MovieRepository;

@Service
public class MovieService {
    
    @Value("${omdb.apiKey}")
    private String apiKey;

    private final String omdbApiUrl = "http://www.omdbapi.com/";

    private final RestTemplate restTemplate;
    private final MovieRepository movieRepository;

    public MovieService(@Value("${omdb.apiKey}") String apiKey, RestTemplate restTemplate, MovieRepository movieRepository) {
        this.apiKey = apiKey;
        this.restTemplate = restTemplate;
        this.movieRepository = movieRepository;
    }

    public List<Movie> getAllMovies() {
        return movieRepository.findAllByOrderByIdAsc();
    }

    public MoviePageResponse getPageMovies(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Movie> moviesPage = movieRepository.findAll(pageable);

        List<Movie> movies = moviesPage.getContent();
        long totalMovies = movieRepository.count();

        return new MoviePageResponse(movies, totalMovies);
    }

     public Movie getMovieById(long id) {
        Optional<Movie> optionalMovie = movieRepository.findById(id);
        if (optionalMovie.isPresent()) {
            return optionalMovie.get();
        }
        return null;
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

    public Movie getMovieFromOmdb(String title) {
        String apiUrl = String.format("%s?apikey=%s&t=%s", omdbApiUrl, apiKey, title);
        OmdbMovie omdbMovie = restTemplate.getForObject(apiUrl, OmdbMovie.class);
        Movie movie = new Movie();
        movie.setAdult(false);
        movie.setGenres(omdbMovie.getGenre());
        movie.setImdbId(omdbMovie.getImdbID());
        movie.setOriginalLanguage(omdbMovie.getLanguage());
        movie.setOriginalTitle(omdbMovie.getTitle());
        movie.setOverview(omdbMovie.getPlot());
        movie.setProductionCountries(omdbMovie.getCountry());
        movie.setReleaseDate(omdbMovie.getReleased());
        movie.setRuntime(Float.parseFloat(omdbMovie.getRuntime().split(" ")[0]));
        movie.setSpokenLanguages(omdbMovie.getLanguage());
        movie.setTitle(omdbMovie.getTitle());
        movie.setVoteAverage(Float.parseFloat(omdbMovie.getImdbRating()));
        movie.setVoteCount(Integer.parseInt(omdbMovie.getImdbVotes().replaceAll(",", "")));
        movie.setImageUrl(omdbMovie.getPoster());
        return movie;
    }
    
}
