package com.media.recommendations.service;

public class MinMaxScaler {
    private float[] min;
    private float[] max;

    public MinMaxScaler(float[] min, float[] max) {
        this.min = min;
        this.max = max;
    }

    public float[] inverseTransform(float[] scaledData) {
        int numFeatures = min.length;
        float[] originalData = new float[scaledData.length];
        for (int i = 0; i < numFeatures; i++) {
            originalData[i] = scaledData[i] * (max[i] - min[i]) + min[i];
        }
        System.arraycopy(scaledData, numFeatures, originalData, numFeatures, scaledData.length - numFeatures);
        return originalData;
    }
}
