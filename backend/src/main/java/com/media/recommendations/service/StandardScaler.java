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

    public float transform(float dataPoint, int index) {
        return (float)((dataPoint - mean[index]) / std[index]);
    }
    
    public float[] inverseTransform(float[] scaledFeatures) {
        float[] originalFeatures = new float[scaledFeatures.length];
        for (int i = 0; i < scaledFeatures.length; i++) {
            originalFeatures[i] = scaledFeatures[i] * std[i] + mean[i];
        }
        return originalFeatures;
    }

    public float inverseTransform(float scaledDataPoint, int index) {
        return scaledDataPoint * std[index] + mean[index];
    }
}
