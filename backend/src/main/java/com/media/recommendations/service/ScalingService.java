package com.media.recommendations.service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.Locale;

import org.json.JSONObject;
import org.springframework.stereotype.Service;

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
        return null;
    }
    
    public float[] rescaleSongFeatures(float[] scaledFeatures, String filePath) {
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
        String[] encoder = jsonArrayToStringArray(jsonObject.getJSONArray("genre_encoding"));
        MinMaxScaler inputScaler = new MinMaxScaler(inputMin, inputMax);

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd MMM yyyy", Locale.ENGLISH);
        LocalDate releaseDate = LocalDate.parse(originalFeatures.getDate(), formatter);
        LocalDate epochStart = LocalDate.of(1970, 1, 1);
        float days = ChronoUnit.DAYS.between(epochStart, releaseDate);

        float scaledDate = inputScaler.scale(days, 0);
        float scaledBoxOffice = inputScaler.scale(originalFeatures.getBoxOffice() / 1_000_000f, 2);
        float scaledImdbRating = inputScaler.scale((float)originalFeatures.getImdbRating(), 3);
        float scaledRuntime = inputScaler.scale(originalFeatures.getRuntime(), 1);
       
        float[] genreEncoded = new float[encoder.length];
        Integer genreIndex = java.util.Arrays.asList(encoder).indexOf(originalFeatures.getGenre());
        if (genreIndex != null && genreIndex != -1) {
            genreEncoded[genreIndex] = 1;
        }

        float[] features = new float[genreEncoded.length + 4];
        features[0] = scaledDate;
        features[1] = scaledBoxOffice;
        features[2] = scaledImdbRating;
        features[3] = scaledRuntime;
        System.arraycopy(genreEncoded, 0, features, 4, genreEncoded.length);

        return features;
    }

    public NeuralModelMovieFeatures rescaleMovieFeatures(float[] scaledFeatures, String filePath) {
        JSONObject jsonObject = getJsonObject(filePath);
        return null;
    }
}
