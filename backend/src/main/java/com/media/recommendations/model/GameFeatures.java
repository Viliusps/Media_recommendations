package com.media.recommendations.model;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class GameFeatures {
    private String name;
    private String releaseDate;
    private List<String> genres;
    private double rating;
    private int playtime;

    @Override
    public String toString() {
        return "GameFeatures{" +
                "name='" + name + '\'' +
                ", releaseDate='" + releaseDate + '\'' +
                ", genres=" + genres +
                ", rating=" + rating +
                ", playtime=" + playtime +
                '}';
    }
}
