package com.media.recommendations.model.requests;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class GetClosestMovieRequest {
    private String genres;
    private int year;
    private int runtime;
}
