package com.media.recommendations.model.responses;

import java.util.List;

import com.media.recommendations.model.Movie;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MoviePageResponse {
    private List<Movie> movies;
    private long totalMovies;
}
