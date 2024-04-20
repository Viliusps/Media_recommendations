package com.media.recommendations.model.responses;

import com.media.recommendations.model.Movie;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TempPairsResponse {
    private Movie firstMovie;
    private Movie secondMovie;
}
