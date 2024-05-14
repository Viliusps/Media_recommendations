package com.media.recommendations.service;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

public class StandardScalerTest {

    @Test
    public void testTransformSingleValue() {
        StandardScaler scaler = new StandardScaler(new float[]{10f}, new float[]{2f});
        assertEquals(0.5f, scaler.transform(11f, 0), "Transform single value failed.");
    }

    @Test
    public void testTransformArray() {
        StandardScaler scaler = new StandardScaler(new float[]{0f, 10f}, new float[]{1f, 2f});
        float[] features = {1f, 14f};
        float[] expected = {1f, 2f};
        assertArrayEquals(expected, scaler.transform(features), "Transform array failed.");
    }

    @Test
    public void testInverseTransformSingleValue() {
        StandardScaler scaler = new StandardScaler(new float[]{5f}, new float[]{3f});
        assertEquals(14f, scaler.inverseTransform(3f, 0), "Inverse transform single value failed.");
    }

    @Test
    public void testInverseTransformArray() {
        StandardScaler scaler = new StandardScaler(new float[]{0f, 2f}, new float[]{1f, 2f});
        float[] scaledFeatures = {3f, 1f};
        float[] expected = {3f, 4f};
        assertArrayEquals(expected, scaler.inverseTransform(scaledFeatures), "Inverse transform array failed.");
    }

    @Test
    public void testTransformAndInverseTransformArray() {
        StandardScaler scaler = new StandardScaler(new float[]{3f, 8f}, new float[]{1f, 2f});
        float[] original = {4f, 12f};
        float[] transformed = scaler.transform(original);
        assertArrayEquals(original, scaler.inverseTransform(transformed), "Transform and inverse transform array failed.");
    }

    @Test
    public void testHandleDivisionByZero() {
        StandardScaler scaler = new StandardScaler(new float[]{0f}, new float[]{0f});
        assertEquals(Float.POSITIVE_INFINITY, scaler.transform(1f, 0), "Division by zero not handled correctly.");
    }

    @Test
    public void testTransformArrayWithMismatchLength() {
        StandardScaler scaler = new StandardScaler(new float[]{0f}, new float[]{1f});
        float[] features = {0f, 1f};
        assertThrows(ArrayIndexOutOfBoundsException.class, () -> scaler.transform(features), "Exception for mismatched array lengths not thrown.");
    }
}
