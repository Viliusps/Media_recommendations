package com.media.recommendations.service;

import org.json.JSONObject;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.io.IOException;

public class ScalerInitializer {
    private StandardScaler inputScaler;
    private MinMaxScaler outputScaler;

    public void initializeScalers(String filePath) throws IOException {
        String content = new String(Files.readAllBytes(Paths.get(filePath)));
        JSONObject jsonObject = new JSONObject(content);

        float[] inputMean = jsonArrayToFloatArray(jsonObject.getJSONArray("input_mean"));
        float[] inputStd = jsonArrayToFloatArray(jsonObject.getJSONArray("input_std"));
        float[] outputMin = jsonArrayToFloatArray(jsonObject.getJSONArray("output_min"));
        float[] outputMax = jsonArrayToFloatArray(jsonObject.getJSONArray("output_max"));

        inputScaler = new StandardScaler(inputMean, inputStd);
        outputScaler = new MinMaxScaler(outputMin, outputMax);
    }

    private float[] jsonArrayToFloatArray(org.json.JSONArray jsonArray) {
        float[] array = new float[jsonArray.length()];
        for (int i = 0; i < jsonArray.length(); i++) {
            array[i] = jsonArray.getFloat(i);
        }
        return array;
    }

    public StandardScaler getInputScaler() {
        return inputScaler;
    }

    public MinMaxScaler getOutputScaler() {
        return outputScaler;
    }
}
