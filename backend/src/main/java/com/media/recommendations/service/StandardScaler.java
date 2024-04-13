package com.media.recommendations.service;

public class StandardScaler {
    private float[] mean;
    private float[] std;

    public StandardScaler(float[] mean, float[] std) {
        this.mean = mean;
        this.std = std;
    }

    public float[] transform(float[] features) {
        float[] scaledFeatures = new float[features.length];
        for (int i = 0; i < features.length; i++) {
            scaledFeatures[i] = (features[i] - mean[i]) / std[i];
        }
        return scaledFeatures;
    }
}
