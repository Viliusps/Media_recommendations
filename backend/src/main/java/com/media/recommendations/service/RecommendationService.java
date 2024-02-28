package com.media.recommendations.service;

import java.time.LocalDate;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.media.recommendations.model.Game;
import com.media.recommendations.model.Movie;
import com.media.recommendations.model.Recommendation;
import com.media.recommendations.model.Song;
import com.media.recommendations.model.requests.ChatRequest;
import com.media.recommendations.model.requests.RecommendationRatingRequest;
import com.media.recommendations.model.requests.RecommendationRequest;
import com.media.recommendations.model.responses.ChatResponse;
import com.media.recommendations.model.responses.RecommendationResponse;
import com.media.recommendations.repository.RecommendationRepository;

@Service
public class RecommendationService {
    @Qualifier("openaiRestTemplate")
    @Autowired
    private RestTemplate restTemplate;
    
    @Value("${OAI.model}")
    private String model;
    
    @Value("${OAI.api.url}")
    private String apiUrl;

    private SongService songService;

    private MovieService movieService;

    private GameService gameService;

    private RecommendationRepository recommendationRepository;

    public RecommendationService(@Qualifier("openaiRestTemplate")@Autowired RestTemplate restTemplate, @Value("${OAI.model}") String model, @Value("${OAI.api.url}") String apiUrl,
        SongService songService, MovieService movieService, GameService gameService, RecommendationRepository recommendationRepository)
    {
        this.restTemplate = restTemplate;
        this.model = model;
        this.apiUrl = apiUrl;
        this.songService = songService;
        this.movieService = movieService;
        this.gameService = gameService;
        this.recommendationRepository = recommendationRepository;
    }

    public RecommendationResponse getRecommendation(RecommendationRequest originalRequest) {
        String prompt = "";

        switch(originalRequest.getRecommendingType()) {
            case "Song":
                prompt += "Recommend me a song, based on ";
                switch(originalRequest.getRecommendingByType()){
                    case "Song":
                        prompt += "a song that I like. The song's name is " + originalRequest.getRecommendingBy() + ". Reply only with a name of your song, add nothing else.";
                        break;
                    case "Movie":
                        prompt += "a movie that I like. The movie's name is " + originalRequest.getRecommendingBy() + ". Reply only with a name of your song, add nothing else.";
                        break;
                    case "Spotify":
                        prompt += "a list of songs that I like. Here is a list of songs, separated by a comma and a space: " + originalRequest.getRecommendingBy() + " . Reply only with a name of your song, add nothing else.";
                        break;
                    case "Game":
                        prompt += "a game that I like. The game's name is " + originalRequest.getRecommendingBy() + ". Reply only with a name of your song, add nothing else.";
                        break;
                }
                break;
            case "Movie":
                prompt += "Recommend me a movie, based on ";
                switch(originalRequest.getRecommendingByType()){
                        case "Song":
                            prompt += "a song that I like. The song's name is " + originalRequest.getRecommendingBy() + ". Reply only with a name of your movie, add nothing else.";
                            break;
                        case "Movie":
                            prompt += "a movie that I like. The movie's name is " + originalRequest.getRecommendingBy() + ". Reply only with a name of your movie, add nothing else.";
                            break;
                        case "Spotify":
                            prompt += "a list of songs that I like. Here is a list of songs, separated by a comma and a space: " + originalRequest.getRecommendingBy() + " . Reply only with a name of your movie, add nothing else.";
                            break;
                        case "Game":
                            prompt += "a game that I like. The game's name is " + originalRequest.getRecommendingBy() + ". Reply only with a name of your movie, add nothing else.";
                            break;
                    }
                    break;
            case "Game":
                prompt += "Recommend me a game, based on ";
                switch(originalRequest.getRecommendingByType()){
                        case "Song":
                            prompt += "a song that I like. The song's name is " + originalRequest.getRecommendingBy() + ". Reply only with the name of your game, add nothing else.";
                            break;
                        case "Movie":
                            prompt += "a movie that I like. The movie's name is " + originalRequest.getRecommendingBy() + ". Reply only with the name of your game, add nothing else.";
                            break;
                        case "Spotify":
                            prompt += "a list of songs that I like. Here is a list of songs, separated by a comma and a space: " + originalRequest.getRecommendingBy() + " . Reply only with the name of your game, add nothing else.";
                            break;
                        case "Game":
                            prompt += "a game that I like. The game's name is " + originalRequest.getRecommendingBy() + ". Reply only with a name of your game, add nothing else.";
                            break;
                    }
                    break;

        }
        ChatRequest request = new ChatRequest(model, prompt);
        ChatResponse response = restTemplate.postForObject(apiUrl, request, ChatResponse.class);
        String chatGPTresponse = response.getChoices().get(0).getMessage().getContent();

        Movie movie = new Movie();
        Song song = new Song();
        Game game = new Game();
    

        if(originalRequest.getRecommendingType().compareTo("Song") == 0)
        {
            song = songService.getSongByNameFromSpotify(chatGPTresponse);
        }

        else if(originalRequest.getRecommendingType().compareTo("Movie") == 0)
        {
            movie = movieService.getMovieFromOmdb(chatGPTresponse);
        }

        else if(originalRequest.getRecommendingType().compareTo("Game") == 0)
        {
            game = gameService.getGameFromRAWG(chatGPTresponse);
        }
        
        if (response == null || response.getChoices() == null || response.getChoices().isEmpty()) {
            return null;
        }
        Movie originalMovie = new Movie();
        Song originalSong = new Song();
        Game originalGame = new Game();

        if(originalRequest.getRecommendingByType().compareTo("Song") == 0)
        {
            originalSong = songService.getSongByNameFromSpotify(originalRequest.getRecommendingBy());
        }

        else if(originalRequest.getRecommendingByType().compareTo("Movie") == 0)
        {
            originalMovie = movieService.getMovieFromOmdb(originalRequest.getRecommendingBy());
        }

        else if(originalRequest.getRecommendingByType().compareTo("Game") == 0)
        {
            originalGame = gameService.getGameFromRAWG(originalRequest.getRecommendingBy());
        }

        return new RecommendationResponse(originalRequest.getRecommendingType(), song, game, movie, originalRequest.getRecommendingByType(), originalSong, originalGame, originalMovie);
    }

    public void rateRecommendation(RecommendationRatingRequest request) {
        System.out.println("Beginning");
        String recommendingType = request.getRecommendingType();
        String recommendingByType = request.getRecommendingByType();
        Object recommending = request.getRecommending();
        Object recommendingBy = request.getRecommendingBy();
        Boolean rating = request.getRating();

        Movie movie = null;
        Song song = null;
        Game game = null;
        Movie movieBy = null;
        Song songBy = null;
        Game gameBy = null;

        long recommendingID = -1;
        long recommendingByID = -1;

        ObjectMapper mapper = new ObjectMapper();

        if ("Movie".equals(recommendingType)) {
            System.out.println("IF check");
            System.out.println(recommending);
            movie = mapper.convertValue(recommending, Movie.class);
            if(!movieService.existsMovie(movie)) {
                Movie newMovie = movieService.createMovie(movie);
                recommendingID = newMovie.getId();
            } else {
                Movie newMovie = movieService.getByIMDBId(movie.getImdbID());
                recommendingID = newMovie.getId();
            }
        } else if ("Song".equals(recommendingType)) {
            song = mapper.convertValue(recommending, Song.class);
            if(!songService.existsSong(song)) {
                Song newSong = songService.createSong(song);
                recommendingID = newSong.getId();
            } else {
                Song newSong = songService.getByISRC(song.getIsrc());
                recommendingID = newSong.getId();
            }
        } else if ("Game".equals(recommendingType)) {
            game = mapper.convertValue(recommending, Game.class);
            if(!gameService.existsGame(game)) {
                Game newGame = gameService.createGame(game);
                recommendingID = newGame.getId();
            } else {
                Game newGame = gameService.getByName(game.getName());
                recommendingID = newGame.getId();
            }
        }

        if ("Movie".equals(recommendingByType)) {
            movieBy = mapper.convertValue(recommendingBy, Movie.class);
            if(!movieService.existsMovie(movieBy)) {
                Movie newMovie = movieService.createMovie(movieBy);
                recommendingByID = newMovie.getId();
            }  else {
                Movie newMovie = movieService.getByIMDBId(movieBy.getImdbID());
                recommendingByID = newMovie.getId();
            }
        } else if ("Song".equals(recommendingByType)) {
            songBy = mapper.convertValue(recommendingBy, Song.class);
            if(!songService.existsSong(songBy)) {
                Song newSong = songService.createSong(songBy);
                recommendingByID = newSong.getId();
            }  else {
                Song newSong = songService.getByISRC(songBy.getIsrc());
                recommendingID = newSong.getId();
            }
        } else if ("Game".equals(recommendingByType)) {
            gameBy = mapper.convertValue(recommendingBy, Game.class);
            if(!gameService.existsGame(gameBy)) {
                Game newGame = gameService.createGame(gameBy);
                recommendingByID = newGame.getId();
            } else {
                Game newGame = gameService.getByName(gameBy.getName());
                recommendingID = newGame.getId();
            }
        }

        if(recommendingByID != -1 && recommendingID != -1) {
            System.out.println("Saving");
            Recommendation recommendation = new Recommendation();
            recommendation.setDate(LocalDate.now());
            recommendation.setFirst(recommendingByID);
            recommendation.setSecond(recommendingID);
            recommendation.setRating(rating);
            recommendation.setFirstType(recommendingByType);
            recommendation.setSecondType(recommendingType);
            recommendationRepository.save(recommendation);
        }
    }
    
}
