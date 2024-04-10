package com.media.recommendations.model.responses;

import java.util.Map;

import com.media.recommendations.model.Movie;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TempResponse {
    private Movie movie;
    private Map<String, String> song;
}
