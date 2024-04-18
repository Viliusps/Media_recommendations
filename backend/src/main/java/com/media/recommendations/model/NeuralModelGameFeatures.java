package com.media.recommendations.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class NeuralModelGameFeatures {
    private String genre;
    private int playtime;
    private int year;
    private double rating;
}
