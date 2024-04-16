package com.media.recommendations.service;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
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
        Pageable pageable = PageRequest.of(page, size, Sort.by("popularity").descending());
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
        return movieRepository.existsByimdbID(movie.getImdbID());
    }

    public Movie getByIMDBId(String IMDB) {
        return movieRepository.getByimdbID(IMDB);
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

    public Movie getMovieFromOmdbByIMBDID(String imdbId) {
        String apiUrl = String.format("%s?apikey=%s&i=%s", omdbApiUrl, apiKey, imdbId);
        Movie omdbMovie = restTemplate.getForObject(apiUrl, Movie.class);
        return omdbMovie;
    }

    public MoviePageResponse search(String search) {
        List<Movie> found = movieRepository.findByTitleContaining(search);
        MoviePageResponse response = new MoviePageResponse(found, found.size());
        return response;
    }

    public Movie getClosestMovieFromFeatures(String genre, int year, int runtime, Double imdbRating) {
        String closestRow = "";
        double minDistance = Double.MAX_VALUE;
        String filePath = "movieFeaturesFinal.tsv";
        double threshold = 0.1;
        System.out.println("Model recommendation: " + genre + " " + year + " " + runtime + " " + imdbRating);

        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;
            reader.readLine();
            while ((line = reader.readLine()) != null) {
                String[] values = line.split("\t");
                List<String> rowGenres = Arrays.asList(values[8].split(","));
                int rowYear = values[5].equals("\\N") ? 0 : Integer.parseInt(values[5]);
                int rowRuntime = values[7].equals("\\N") ? 0 : Integer.parseInt(values[7]);
                Double rowRating = values[9].equals("\\N") ? 0 : Double.parseDouble(values[9]);

                boolean genresMatch = genre.isEmpty() || rowGenres.contains(genre);

                double distance = Math.sqrt(Math.pow(year - rowYear, 2) +
                                            Math.pow(runtime - rowRuntime, 2) +
                                            Math.pow(imdbRating - rowRating, 2));
                
                if (genresMatch) {
                    distance *= 0.75;
                }

                if (distance < minDistance) {
                    minDistance = distance;
                    closestRow = line;
                    if (minDistance <= threshold) {
                        break;
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
        System.out.println("Closest row: " + closestRow);

        String imdbId = closestRow.split("\t")[0];
        return getMovieFromOmdbByIMBDID(imdbId);
    }

    public void increasePopularity(Movie movie) {
        Movie movieFromDb = getByIMDBId(movie.getImdbID());
        movieFromDb.setPopularity(movieFromDb.getPopularity() + 1);
        movieRepository.save(movieFromDb);
    }
}
