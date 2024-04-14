package com.media.recommendations.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class NeuralModelMovieFeatures {
    private String genre;
    private String date;
    private int boxOffice;
    private double imdbRating;
    private int runtime;
}
