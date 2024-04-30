package com.media.recommendations.service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.tensorflow.Result;
import org.tensorflow.SavedModelBundle;
import org.tensorflow.Tensor;
import org.tensorflow.ndarray.FloatNdArray;
import org.tensorflow.ndarray.NdArrays;
import org.tensorflow.ndarray.Shape;
import org.tensorflow.ndarray.buffer.FloatDataBuffer;
import org.tensorflow.types.TFloat32;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.media.recommendations.model.Game;
import com.media.recommendations.model.Movie;
import com.media.recommendations.model.NeuralModelGameFeatures;
import com.media.recommendations.model.NeuralModelMovieFeatures;
import com.media.recommendations.model.Recommendation;
import com.media.recommendations.model.Song;
import com.media.recommendations.model.User;
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

    private UserService userService;

    private ScalingService scalingService;

    private RecommendationRepository recommendationRepository;

    public RecommendationService(@Qualifier("openaiRestTemplate")@Autowired RestTemplate restTemplate, @Value("${OAI.model}") String model, @Value("${OAI.api.url}") String apiUrl,
        SongService songService, MovieService movieService, GameService gameService, RecommendationRepository recommendationRepository, UserService userService, ScalingService scalingService)
    {
        this.restTemplate = restTemplate;
        this.model = model;
        this.apiUrl = apiUrl;
        this.songService = songService;
        this.movieService = movieService;
        this.gameService = gameService;
        this.recommendationRepository = recommendationRepository;
        this.userService = userService;
        this.scalingService = scalingService;
    }

    public RecommendationResponse getRecommendation(RecommendationRequest originalRequest) {
        String prompt = "";
        switch(originalRequest.getRecommendingType()) {
            case "Song":
                prompt += "Recommend me a song, based on ";
                switch(originalRequest.getRecommendingByType()){
                    case "Song":
                        prompt += "a song that I like. The song's name is " + originalRequest.getRecommendingBy() + ". Reply only with a name of your song, do not include artist.";
                        break;
                    case "Movie":
                        prompt += "a movie that I like. The movie's name is " + originalRequest.getRecommendingBy() + ". Reply only with a name of your song, do not include artist.";
                        break;
                    case "Spotify":
                        String songNames = songService.getSpotifyHistoryNames(originalRequest.getUsername());
                        prompt += "a list of songs that I like. Here is a list of songs, separated by a comma and a space: " + songNames + " . Reply only with a name of your song, do not include artist.";
                        break;
                    case "Game":
                        prompt += "a game that I like. The game's name is " + originalRequest.getRecommendingBy() + ". Reply only with a name of your song, do not include artist.";
                        break;
                    case "Steam":
                        String gameNames = gameService.getSteamHistoryNames(originalRequest.getUsername());
                        prompt += "a list of games that I like. Here is a list of games, separated by a comma and a space: " + gameNames + " . Reply only with a name of your song, do not include artist.";
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
                            String songNames = songService.getSpotifyHistoryNames(originalRequest.getUsername());
                            prompt += "a list of songs that I like. Here is a list of songs, separated by a comma and a space: " + songNames + " . Reply only with a name of your movie, add nothing else.";
                            break;
                        case "Game":
                            prompt += "a game that I like. The game's name is " + originalRequest.getRecommendingBy() + ". Reply only with a name of your movie, add nothing else.";
                            break;
                        case "Steam":
                            String gameNames = gameService.getSteamHistoryNames(originalRequest.getUsername());
                            prompt += "a list of games that I like. Here is a list of games, separated by a comma and a space: " + gameNames + " . Reply only with a name of your movie, add nothing else.";
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
                            String songNames = songService.getSpotifyHistoryNames(originalRequest.getUsername());
                            prompt += "a list of songs that I like. Here is a list of songs, separated by a comma and a space: " + songNames + " . Reply only with the name of your game, add nothing else.";
                            break;
                        case "Game":
                            prompt += "a game that I like. The game's name is " + originalRequest.getRecommendingBy() + ". Reply only with a name of your game, add nothing else.";
                            break;
                        case "Steam":
                            String gameNames = gameService.getSteamHistoryNames(originalRequest.getUsername());
                            prompt += "a list of games that I like. Here is a list of games, separated by a comma and a space: " + gameNames + " . Reply only with a name of your game, add nothing else.";
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
            song.setTitle(chatGPTresponse);
            song = songService.getSongByNameFromSpotify(chatGPTresponse);
        }

        else if(originalRequest.getRecommendingType().compareTo("Movie") == 0)
        {
            movie.setTitle(chatGPTresponse);
            movie = movieService.getMovieFromOmdb(chatGPTresponse);
        }

        else if(originalRequest.getRecommendingType().compareTo("Game") == 0)
        {
            game.setName(chatGPTresponse);
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
            originalSong = songService.getSongByISRCFromSpotify(originalRequest.getRecommendingByID());
            originalSong = songService.getSongFeatures(originalSong);
            if(!songService.existsSong(originalSong)) {
                songService.createSong(originalSong);
            }
            songService.increasePopularity(originalSong);
        }

        else if(originalRequest.getRecommendingByType().compareTo("Movie") == 0)
        {
            originalMovie = movieService.getMovieFromOmdbByIMBDID(originalRequest.getRecommendingByID());
            if(!movieService.existsMovie(originalMovie)) {
                movieService.createMovie(originalMovie);
            }
            movieService.increasePopularity(originalMovie);
        }

        else if(originalRequest.getRecommendingByType().compareTo("Game") == 0)
        {
            originalGame = gameService.getGameFromRAWGByID(originalRequest.getRecommendingByID());
            if(!gameService.existsGame(originalGame)) {
                gameService.createGame(originalGame);
            }
            gameService.increasePopularity(originalGame);
        }

        return new RecommendationResponse(originalRequest.getRecommendingType(), song, game, movie, originalRequest.getRecommendingByType(), originalSong, originalGame, originalMovie);
    }

    public void rateRecommendation(RecommendationRatingRequest request) {
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
            song = songService.getSongFeatures(song);
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
            songBy = songService.getSongFeatures(songBy);
            if(!songService.existsSong(songBy)) {
                Song newSong = songService.createSong(songBy);
                recommendingByID = newSong.getId();
            }  else {
                Song newSong = songService.getByISRC(songBy.getIsrc());
                recommendingByID = newSong.getId();
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
            Recommendation recommendation = new Recommendation();
            recommendation.setDate(LocalDate.now());
            recommendation.setFirst(recommendingByID);
            recommendation.setSecond(recommendingID);
            recommendation.setRating(rating);
            recommendation.setFirstType(recommendingByType);
            recommendation.setSecondType(recommendingType);
            recommendation.setUser(userService.userByUsername(request.getUsername()));
            if(!recommendationExists(recommendation)) {
                recommendationRepository.save(recommendation);
                Long count = recommendationRepository.countByFirstTypeAndSecondTypeAndPositive(recommendingByType, recommendingType);
                System.out.println("Count: " + count);
                if(count % 100 == 0) {
                    System.out.println("UPDATE MODEL");
                    executePythonScript(recommendingByType, recommendingType);
                }
            }
        }
    }

    private Boolean recommendationExists(Recommendation recommendation) {
        return recommendationRepository.existsByFirstAndSecondAndRatingAndFirstTypeAndSecondTypeAndUser(recommendation.getFirst(),
            recommendation.getSecond(), recommendation.isRating(), recommendation.getFirstType(), recommendation.getSecondType(), recommendation.getUser());
    }

    public RecommendationResponse getModelRecommendation(RecommendationRequest originalRequest) {
        String modelPath = "neuralModel/model_";
        String scalerPath = "neuralModel/scalingParameters/scaling_parameters_";
        float[] features = new float[0];

        Movie originalMovie = new Movie();
        Song originalSong = new Song();
        Game originalGame = new Game();

        //SHOULD REUSE FROM CHATGPT METHOD!!!!!
        if(originalRequest.getRecommendingByType().compareTo("Song") == 0)
        {
            originalSong = songService.getSongByISRCFromSpotify(originalRequest.getRecommendingByID());
            originalSong = songService.getSongFeatures(originalSong);
            if(originalSong.getDynamicComplexity() == null) return null;
            if(!songService.existsSong(originalSong)) {
                songService.createSong(originalSong);
            }
        }

        else if(originalRequest.getRecommendingByType().compareTo("Movie") == 0)
        {
            originalMovie = movieService.getMovieFromOmdbByIMBDID(originalRequest.getRecommendingByID());
            if(!movieService.existsMovie(originalMovie)) {
                movieService.createMovie(originalMovie);
            }
        }

        else if(originalRequest.getRecommendingByType().compareTo("Game") == 0)
        {
            originalGame = gameService.getGameFromRAWGByID(originalRequest.getRecommendingByID());
            if(!gameService.existsGame(originalGame)) {
                gameService.createGame(originalGame);
            }
        }

        switch (originalRequest.getRecommendingType()) {
            case "Movie":
                switch (originalRequest.getRecommendingByType()) {
                    case "Movie":
                        scalerPath += "mm.json";
                        modelPath += "mm";
                        NeuralModelMovieFeatures movieFeatures = prepareMovieFeatures(originalMovie);
                        features = scalingService.scaleMovieFeatures(movieFeatures, scalerPath);
                        break;
                    case "Song":
                        scalerPath += "sm.json";
                        modelPath += "sm";
                        float[] songFeatures = prepareSongFeatures(originalSong);
                        features = scalingService.scaleSongFeatures(songFeatures, scalerPath);
                        break;
                    case "Spotify":
                        scalerPath += "sm.json";
                        modelPath += "sm";
                        Song averageSong = songService.calculateAverage(originalRequest.getUsername());
                        originalSong = averageSong;
                        float[] spotifySong = prepareSongFeatures(averageSong);
                        features = scalingService.scaleSongFeatures(spotifySong, scalerPath);
                        break;
                    case "Game":
                        scalerPath += "gm.json";
                        modelPath += "gm";
                        NeuralModelGameFeatures gameFeatures = prepareGameFeatures(originalGame);
                        features = scalingService.scaleGameFeatures(gameFeatures, scalerPath);
                        break;
                    case "Steam":
                        scalerPath += "gm.json";
                        modelPath += "gm";
                        Game averageGame = gameService.calculateAverage(originalRequest.getUsername());
                        originalGame = averageGame;
                        NeuralModelGameFeatures avgGameFeatures = prepareGameFeatures(averageGame);
                        features = scalingService.scaleGameFeatures(avgGameFeatures, scalerPath);
                        break;
                    default:
                        break;
                }
                break;
            case "Song":
                switch (originalRequest.getRecommendingByType()) {
                    case "Movie":
                        scalerPath += "ms.json";
                        modelPath += "ms";
                        NeuralModelMovieFeatures movieFeatures = prepareMovieFeatures(originalMovie);
                        features = scalingService.scaleMovieFeatures(movieFeatures, scalerPath);
                        break;
                    case "Song":
                        scalerPath += "ss.json";
                        modelPath += "ss";
                        float[] arr = prepareSongFeatures(originalSong);
                        features = scalingService.scaleSongFeatures(arr, scalerPath);
                        break;
                    case "Spotify":
                        scalerPath += "ss.json";
                        modelPath += "ss";
                        Song averageSong = songService.calculateAverage(originalRequest.getUsername());
                        originalSong = averageSong;
                        float[] spotifySong = prepareSongFeatures(averageSong);
                        features = scalingService.scaleSongFeatures(spotifySong, scalerPath);
                        break;
                    case "Game":
                        scalerPath += "gs.json";
                        modelPath += "gs";
                        NeuralModelGameFeatures gameFeatures = prepareGameFeatures(originalGame);
                        features = scalingService.scaleGameFeatures(gameFeatures, scalerPath);
                        break;
                    case "Steam":
                        scalerPath += "gs.json";
                        modelPath += "gs";
                        Game averageGame = gameService.calculateAverage(originalRequest.getUsername());
                        originalGame = averageGame;
                        NeuralModelGameFeatures avgGameFeatures = prepareGameFeatures(averageGame);
                        features = scalingService.scaleGameFeatures(avgGameFeatures, scalerPath);
                        break;
                    default:
                        break;
                }
                break;
            case "Game":
                switch (originalRequest.getRecommendingByType()) {
                    case "Movie":
                        scalerPath += "mg.json";
                        modelPath += "mg";
                        NeuralModelMovieFeatures movieFeatures = prepareMovieFeatures(originalMovie);
                        features = scalingService.scaleMovieFeatures(movieFeatures, scalerPath);
                        break;
                    case "Song":
                        scalerPath += "sg.json";
                        modelPath += "sg";
                        float[] arr = prepareSongFeatures(originalSong);
                        features = scalingService.scaleSongFeatures(arr, scalerPath);
                        break;
                    case "Spotify":
                        scalerPath += "sg.json";
                        modelPath += "sg";
                        Song averageSong = songService.calculateAverage(originalRequest.getUsername());
                        originalSong = averageSong;
                        float[] spotifySong = prepareSongFeatures(averageSong);
                        features = scalingService.scaleSongFeatures(spotifySong, scalerPath);
                        break;
                    case "Game":
                        scalerPath += "gg.json";
                        modelPath += "gg";
                        NeuralModelGameFeatures gameFeatures = prepareGameFeatures(originalGame);
                        features = scalingService.scaleGameFeatures(gameFeatures, scalerPath);
                        break;
                    case "Steam":
                        scalerPath += "gg.json";
                        modelPath += "gg";
                        Game averageGame = gameService.calculateAverage(originalRequest.getUsername());
                        originalGame = averageGame;
                        NeuralModelGameFeatures avgGameFeatures = prepareGameFeatures(averageGame);
                        features = scalingService.scaleGameFeatures(avgGameFeatures, scalerPath);
                        break;
                    default:
                        break;
                }
                break;
            default:
                break;
        }

        SavedModelBundle model = SavedModelBundle.load(modelPath, "serve");
        FloatNdArray input_matrix = NdArrays.ofFloats(Shape.of(1, features.length));
        input_matrix.set(NdArrays.vectorOf(features), 0);
        Tensor input_tensor = TFloat32.tensorOf(input_matrix);

        Map<String, Tensor> feed_dict = new HashMap<>();
        feed_dict.put("dense_input", input_tensor);
        Result result = model.function("serving_default").call(feed_dict);
        Optional<Tensor> output = result.get("output_0");
        Movie movie = new Movie();
        Song song = new Song();
        Game game = new Game();

        if(output.isPresent()) {
            Tensor outputTensor = output.get();
            FloatDataBuffer resultsBuffer = outputTensor.asRawTensor().data().asFloats();
            float[] results = new float[(int)resultsBuffer.size()];
            resultsBuffer.read(results);
            switch (originalRequest.getRecommendingType()) {
                case "Movie":
                    NeuralModelMovieFeatures movieOutput = scalingService.rescaleMovieFeatures(results, scalerPath);
                    movie = movieService.getClosestMovieFromFeatures(movieOutput.getGenre(), movieOutput.getYear(), movieOutput.getRuntime(), movieOutput.getImdbRating());
                    break;
                case "Song":
                    float[] songOutput = scalingService.rescaleSongFeatures(results, scalerPath);
                    String closestString = songService.getClosestSongFromFeatures(songOutput);
                    song = songService.getSongFromSearchResults(closestString);
                    break;
                case "Game":
                    NeuralModelGameFeatures gameOutput = scalingService.rescaleGameFeatures(results, scalerPath);
                    game = gameService.findGameFromFeatures(gameOutput);
                    break;
                default:
                    break;
            }
            
            return new RecommendationResponse(originalRequest.getRecommendingType(), song, game, movie, originalRequest.getRecommendingByType(), originalSong, originalGame, originalMovie);
        }
        return null;
    }

    private float[] prepareSongFeatures(Song song) {
        float[] arr = new float[12];
        arr[0] = Float.parseFloat(song.getBpmHistogramFirstPeakBpmMean());
        arr[1] = Float.parseFloat(song.getDanceability());
        arr[2] = Float.parseFloat(song.getBpmHistogramSecondPeakBpmMedian());
        arr[3] = Float.parseFloat(song.getTuningEqualTemperedDeviation());
        arr[4] = Float.parseFloat(song.getTuningFrequency());
        arr[5] = Float.parseFloat(song.getBpmHistogramSecondPeakBpmMean());
        arr[6] = Float.parseFloat(song.getBpm());
        arr[7] = Float.parseFloat(song.getBpmHistogramFirstPeakBpmMedian());
        arr[8] = Float.parseFloat(song.getMfccZeroMean());
        arr[9] = Float.parseFloat(song.getOnsetRate());
        arr[10] = Float.parseFloat(song.getAverageLoudness());
        arr[11] = Float.parseFloat(song.getDynamicComplexity());
        return arr;
    }

    private NeuralModelMovieFeatures prepareMovieFeatures(Movie movie) {
        Integer votes = Integer.parseInt(movie.getImdbVotes().replace(",", ""));
        Integer runtime = Integer.parseInt(movie.getRuntime().replace(" min", ""));
        String genre = movie.getGenre().split(",")[0];
        return new NeuralModelMovieFeatures(genre, Integer.parseInt(movie.getYear()), votes, Double.parseDouble(movie.getImdbRating()), runtime);
    }

    private NeuralModelGameFeatures prepareGameFeatures(Game game) {
        Integer year = LocalDate.parse(game.getReleaseDate()).getYear();
        String genre = game.getGenres().split(",")[0];
        return new NeuralModelGameFeatures(genre, game.getPlaytime(), year, game.getRating());
    }

    public List<RecommendationResponse> getRecentRecommendations(String username) {
        List<RecommendationResponse> results = new ArrayList<>();
        User user = userService.userByUsername(username);
        List<Recommendation> recommendations = recommendationRepository.getByUser(user);
        Collections.reverse(recommendations);
        for(int i = 0; i < recommendations.size() && i < 5; i++) {
            Recommendation recommendation = recommendations.get(i);
            RecommendationResponse newRecommendation = new RecommendationResponse();

            newRecommendation.setOriginalType(recommendation.getFirstType());
            newRecommendation.setType(recommendation.getSecondType());
            
            long firstId = recommendation.getFirst();
            long secondId = recommendation.getSecond();
            switch (recommendation.getFirstType()) {
                case "Movie":
                    newRecommendation.setOriginalMovie(movieService.getMovieById(firstId));
                    break;
                case "Song":
                    newRecommendation.setOriginalSong(songService.getSongById(firstId));
                    break;
                case "Game":
                    newRecommendation.setOriginalGame(gameService.getGameById(firstId));
                    break;
                default:
                    break;
            }

            switch (recommendation.getSecondType()) {
                case "Movie":
                    newRecommendation.setMovie(movieService.getMovieById(secondId));
                    break;
                case "Song":
                    newRecommendation.setSong(songService.getSongById(secondId));
                    break;
                case "Game":
                    newRecommendation.setGame(gameService.getGameById(secondId));
                    break;
                default:
                    break;
            }

            results.add(newRecommendation);
        }
        return results;
    }
    
    public String executePythonScript(String firstType, String secondType) {
         try {
            ProcessBuilder processBuilder = new ProcessBuilder("python", "neuralModel/modelScripts/neuralModelRetrain.py", firstType, secondType);
            processBuilder.redirectErrorStream(true);
            Process process = processBuilder.start();

            StringBuilder output = new StringBuilder();
            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));

            String line;
            while ((line = reader.readLine()) != null) {
                output.append(line + "\n");
            }

            int exitVal = process.waitFor();
            if (exitVal == 0) {
                return output.toString();
            } else {
                return "Script execution failed!:\n" + output.toString();
            }
        } catch (IOException | InterruptedException e) {
            Thread.currentThread().interrupt();
            return "Exception occurred: " + e.getMessage();
        }
    }
}
