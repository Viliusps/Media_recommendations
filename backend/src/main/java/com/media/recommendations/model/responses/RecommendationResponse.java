package com.media.recommendations.model.responses;

import com.media.recommendations.model.Game;
import com.media.recommendations.model.Movie;
import com.media.recommendations.model.Song;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RecommendationResponse {
    private String type;
    private Song song;
    private Game game;
    private Movie movie;

    private String originalType;
    private Song originalSong;
    private Game originalGame;
    private Movie originalMovie;
}
