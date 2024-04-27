package com.media.recommendations.service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;

import org.json.JSONObject;
import org.springframework.stereotype.Service;

import com.media.recommendations.model.NeuralModelGameFeatures;
import com.media.recommendations.model.NeuralModelMovieFeatures;

@Service
public class ScalingService {

    private JSONObject getJsonObject(String filePath) {
        String content;
        try {
            content = new String(Files.readAllBytes(Paths.get(filePath)));
            return new JSONObject(content);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    private float[] jsonArrayToFloatArray(org.json.JSONArray jsonArray) {
        float[] array = new float[jsonArray.length()];
        for (int i = 0; i < jsonArray.length(); i++) {
            array[i] = jsonArray.getFloat(i);
        }
        return array;
    }

    private String[] jsonArrayToStringArray(org.json.JSONArray jsonArray) {
        String[] array = new String[jsonArray.length()];
        for (int i = 0; i < jsonArray.length(); i++) {
            array[i] = jsonArray.getString(i);
        }
        return array;
    }

    public float[] scaleSongFeatures(float[] originalFeatures, String filePath) {
        JSONObject jsonObject = getJsonObject(filePath);
        float[] inputMean = jsonArrayToFloatArray(jsonObject.getJSONArray("input_mean"));
        float[] inputStd = jsonArrayToFloatArray(jsonObject.getJSONArray("input_std"));
        StandardScaler outputScaler = new StandardScaler(inputMean, inputStd);
        float[] scaledFeatures = outputScaler.transform(originalFeatures);
        System.out.println("Transformed song features: " + Arrays.toString(scaledFeatures));
        return scaledFeatures;
    }
    
    public float[] rescaleSongFeatures(float[] scaledFeatures, String filePath) {
        System.out.println("Features before trying to rescale: " + Arrays.toString(scaledFeatures));
        JSONObject jsonObject = getJsonObject(filePath);
        float[] outputMean = jsonArrayToFloatArray(jsonObject.getJSONArray("output_mean"));
        float[] outputStd = jsonArrayToFloatArray(jsonObject.getJSONArray("output_std"));
        StandardScaler outputScaler = new StandardScaler(outputMean, outputStd);
        float[] rescaled = outputScaler.inverseTransform(scaledFeatures);
        return rescaled;
    }

    public float[] scaleMovieFeatures(NeuralModelMovieFeatures originalFeatures, String filePath) {
        JSONObject jsonObject = getJsonObject(filePath);
        float[] inputMin = jsonArrayToFloatArray(jsonObject.getJSONArray("input_min"));
        float[] inputMax = jsonArrayToFloatArray(jsonObject.getJSONArray("input_max"));
        String[] encoder = jsonArrayToStringArray(jsonObject.getJSONArray("input_movie_genre_encoding"));
        MinMaxScaler inputScaler = new MinMaxScaler(inputMin, inputMax);

        float year = originalFeatures.getYear();

        float scaledYear = inputScaler.scale(year, 0);
        float scaledImdbVotes = inputScaler.scale(originalFeatures.getImdbVotes(), 2);
        float scaledImdbRating = inputScaler.scale((float)originalFeatures.getImdbRating(), 3);
        float scaledRuntime = inputScaler.scale(originalFeatures.getRuntime(), 1);
       
        float[] genreEncoded = new float[encoder.length];
        Integer genreIndex = java.util.Arrays.asList(encoder).indexOf(originalFeatures.getGenre());
        if (genreIndex != null && genreIndex != -1) {
            genreEncoded[genreIndex] = 1;
        }

        float[] features = new float[genreEncoded.length + 4];
        features[0] = scaledYear;
        features[1] = scaledRuntime;
        features[2] = scaledImdbVotes;
        features[3] = scaledImdbRating;
        
        System.arraycopy(genreEncoded, 0, features, 4, genreEncoded.length);

        return features;
    }

    public NeuralModelMovieFeatures rescaleMovieFeatures(float[] scaledFeatures, String filePath) {
        NeuralModelMovieFeatures rescaledFeatures = new NeuralModelMovieFeatures();
        JSONObject jsonObject = getJsonObject(filePath);
        float[] outputMin = jsonArrayToFloatArray(jsonObject.getJSONArray("output_min"));
        float[] outputMax = jsonArrayToFloatArray(jsonObject.getJSONArray("output_max"));
        String[] encoder = jsonArrayToStringArray(jsonObject.getJSONArray("output_movie_genre_encoding"));
        MinMaxScaler inputScaler = new MinMaxScaler(outputMin, outputMax);

        int year = (int) inputScaler.inverseTransform(scaledFeatures[0], 0);
        float rescaledRuntime = inputScaler.inverseTransform(scaledFeatures[1], 1);
        float rescaledImdbVotes = inputScaler.inverseTransform(scaledFeatures[2], 2);
        float rescaledImdbRating = inputScaler.inverseTransform(scaledFeatures[3], 3);

        System.out.println("MOVIE GENRES: " + Arrays.toString(encoder));
        System.out.println("MOVIE RESPONSE GENRES: " + Arrays.toString(scaledFeatures));
        String genre = "";
        double maxVal = scaledFeatures[4];
        int maxInd = 0;
        for (int i = 0; i < encoder.length; i++) {
            if (scaledFeatures[i + 4] > maxVal) {
                maxVal = scaledFeatures[i + 4];
                maxInd = i;
            }
        }
        genre = encoder[maxInd];
        System.out.println("Highest genre value: " + maxVal);
        System.out.println("genre: " + genre);
        rescaledFeatures.setImdbVotes((int)rescaledImdbVotes);
        rescaledFeatures.setImdbRating(rescaledImdbRating);
        rescaledFeatures.setRuntime((int)rescaledRuntime);
        rescaledFeatures.setGenre(genre);
        rescaledFeatures.setYear(year);

        return rescaledFeatures;
    }

    public float[] scaleGameFeatures(NeuralModelGameFeatures features, String filePath) {
        JSONObject jsonObject = getJsonObject(filePath);
        float[] inputMin = jsonArrayToFloatArray(jsonObject.getJSONArray("input_min"));
        float[] inputMax = jsonArrayToFloatArray(jsonObject.getJSONArray("input_max"));
        String[] encoder = jsonArrayToStringArray(jsonObject.getJSONArray("input_game_genre_encoding"));
        MinMaxScaler inputScaler = new MinMaxScaler(inputMin, inputMax);

        float year = features.getYear();
        float scaledYear = inputScaler.scale(year, 0);
        float scaledRating = inputScaler.scale((float)features.getRating(), 1);
        float scaledPlaytime = inputScaler.scale((float)features.getPlaytime(), 2);

        System.out.println("Unscaled genre: " + features.getGenre());
        float[] genreEncoded = new float[encoder.length];
        System.out.println(encoder);
        Integer genreIndex = java.util.Arrays.asList(encoder).indexOf(features.getGenre());
        System.out.println("Index: " + genreIndex);
        if (genreIndex != null && genreIndex != -1) {
            genreEncoded[genreIndex] = 1;
        }

        float[] scaledFeatures = new float[genreEncoded.length + 3];
        scaledFeatures[0] = scaledYear;
        scaledFeatures[1] = scaledRating;
        scaledFeatures[2] = scaledPlaytime;
        System.arraycopy(genreEncoded, 0, scaledFeatures, 3, genreEncoded.length);

        return scaledFeatures;
    }

    public NeuralModelGameFeatures rescaleGameFeatures(float[] scaledFeatures, String filePath) {
        NeuralModelGameFeatures rescaledFeatures = new NeuralModelGameFeatures();
        JSONObject jsonObject = getJsonObject(filePath);

        float[] outputMin = jsonArrayToFloatArray(jsonObject.getJSONArray("output_min"));
        float[] outputMax = jsonArrayToFloatArray(jsonObject.getJSONArray("output_max"));
        String[] encoder = jsonArrayToStringArray(jsonObject.getJSONArray("output_game_genre_encoding"));
        MinMaxScaler inputScaler = new MinMaxScaler(outputMin, outputMax);

        int year = (int) inputScaler.inverseTransform(scaledFeatures[0], 0);
        System.out.println("Rescaled year: " + year);
        
        float rescaledRating = inputScaler.inverseTransform(scaledFeatures[1], 1);
        System.out.println("Rescaled rating: " + rescaledRating);

        float rescaledPlaytime = inputScaler.inverseTransform(scaledFeatures[2], 2);
        System.out.println("Rescaled playtime: " + rescaledPlaytime);
        String genre = "";
        double maxVal = scaledFeatures[3];
        int maxInd = 0;
        for (int i = 0; i < encoder.length; i++) {
            if (scaledFeatures[i + 3] > maxVal) {
                maxVal = scaledFeatures[i + 3];
                maxInd = i;
            }
        }
        genre = encoder[maxInd];
        rescaledFeatures.setPlaytime((int)rescaledPlaytime);
        rescaledFeatures.setRating(rescaledRating);
        rescaledFeatures.setGenre(genre);
        rescaledFeatures.setYear(year);

        return rescaledFeatures;
    }
}
