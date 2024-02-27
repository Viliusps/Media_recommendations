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
import com.media.recommendations.model.responses.MoviePageResponse;
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
        return movieRepository.save(movie);
    }

    public boolean existsMovie(long id) {
        return movieRepository.existsById(id);
    }

    public boolean existsMovie(Movie movie) {
        if (movie == null) {
        return false;
        }
        return movieRepository.existsByImdbID(movie.getImdbID());
    }

    public Movie updateMovie(Long id, Movie movie) {
        Movie movieFromDb = movieRepository.findById(id).get();
        movieFromDb.setActors(movie.getActors());
        movieFromDb.setAwards(movie.getAwards());
        movieFromDb.setBoxOffice(movie.getBoxOffice());
        movieFromDb.setComments(movie.getComments());
        movieFromDb.setCountry(movie.getCountry());
        movieFromDb.setDirector(movie.getDirector());
        movieFromDb.setDvd(movie.getDvd());
        movieFromDb.setGenre(movie.getGenre());
        movieFromDb.setImdbID(movie.getImdbID());
        movieFromDb.setImdbRating(movie.getImdbRating());
        movieFromDb.setImdbVotes(movie.getImdbVotes());
        movieFromDb.setLanguage(movie.getLanguage());
        movieFromDb.setMetascore(movie.getMetascore());
        movieFromDb.setPlot(movie.getPlot());
        movieFromDb.setPoster(movie.getPoster());
        movieFromDb.setProduction(movie.getProduction());
        movieFromDb.setRated(movie.getRated());
        movieFromDb.setRatings(movie.getRatings());
        movieFromDb.setReleased(movie.getReleased());
        movieFromDb.setResponse(movie.getResponse());
        movieFromDb.setRuntime(movie.getRuntime());
        movieFromDb.setTitle(movie.getTitle());
        movieFromDb.setType(movie.getType());
        movieFromDb.setWebsite(movie.getWebsite());
        movieFromDb.setWriter(movie.getWriter());
        movieFromDb.setYear(movie.getYear());
        return movieRepository.save(movieFromDb);
    }

    public void deleteMovie(Long id) {
        movieRepository.deleteById(id);
    }

    public Movie getMovieFromOmdb(String title) {
        String apiUrl = String.format("%s?apikey=%s&t=%s", omdbApiUrl, apiKey, title);
        Movie omdbMovie = restTemplate.getForObject(apiUrl, Movie.class);
        return omdbMovie;
    }

    public MoviePageResponse search(String search) {
        List<Movie> found = movieRepository.findByTitleContaining(search);
        MoviePageResponse response = new MoviePageResponse(found, found.size());
        return response;
    }
    
}
