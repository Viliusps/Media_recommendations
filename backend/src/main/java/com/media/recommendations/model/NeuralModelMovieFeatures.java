package com.media.recommendations.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class NeuralModelMovieFeatures {
    private String genre;
    private int year;
    private int imdbVotes;
    private double imdbRating;
    private int runtime;
}
