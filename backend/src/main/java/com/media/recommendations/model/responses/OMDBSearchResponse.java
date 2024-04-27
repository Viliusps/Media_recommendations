package com.media.recommendations.model.responses;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OMDBSearchResponse {
    private String title;
    private String year;
    private String imdbID;
}
