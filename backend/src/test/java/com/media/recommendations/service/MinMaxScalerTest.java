package com.media.recommendations.service;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

public class MinMaxScalerTest {

    @Test
    public void testScaleSingleValue() {
        MinMaxScaler scaler = new MinMaxScaler(new float[]{0f, 0f}, new float[]{1f, 2f});
        assertEquals(0.25f, scaler.scale(0.5f, 1), "Scaling single value failed.");
    }

    @Test
    public void testInverseTransformSingleValue() {
        MinMaxScaler scaler = new MinMaxScaler(new float[]{0f, 0f}, new float[]{10f, 20f});
        assertEquals(10f, scaler.inverseTransform(1f, 0), "Inverse transform single value failed.");
    }

    @Test
    public void testScaleAndInverseScaleSingleValue() {
        MinMaxScaler scaler = new MinMaxScaler(new float[]{0f}, new float[]{10f});
        float original = 5f;
        float scaled = scaler.scale(original, 0);
        assertEquals(original, scaler.inverseTransform(scaled, 0), "Scaling and inverse scaling single value failed.");
    }

    @Test
    public void testInverseTransformArray() {
        MinMaxScaler scaler = new MinMaxScaler(new float[]{1f, 2f}, new float[]{11f, 22f});
        float[] scaledData = {0f, 0.5f};
        float[] expected = {1f, 12f};
        assertArrayEquals(expected, scaler.inverseTransform(scaledData), "Inverse transform array failed.");
    }

    @Test
    public void testExceptionForMismatchedArrayLengths() {
        MinMaxScaler scaler = new MinMaxScaler(new float[]{0f, 0f, 0f}, new float[]{1f, 2f, 3f});
        float[] scaledData = {0f, 0.5f};
        assertThrows(ArrayIndexOutOfBoundsException.class, () -> {
            scaler.inverseTransform(scaledData);
        }, "Exception for mismatched array lengths not thrown.");
    }

    @Test
    public void testHandleDivisionByZero() {
        MinMaxScaler scaler = new MinMaxScaler(new float[]{0f}, new float[]{0f});
        assertEquals(Float.POSITIVE_INFINITY, scaler.scale(1f, 0), "Division by zero not handled correctly.");
    }
}
