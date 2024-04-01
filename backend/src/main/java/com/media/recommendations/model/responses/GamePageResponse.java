package com.media.recommendations.model.responses;

import java.util.List;

import com.media.recommendations.model.Game;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class GamePageResponse {
    private List<Game> games;
    private long totalGames;
}
