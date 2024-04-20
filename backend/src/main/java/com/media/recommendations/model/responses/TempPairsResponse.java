package com.media.recommendations.model.responses;

import com.media.recommendations.model.Game;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TempPairsResponse {
    private Game firstGame;
    private Game secondGame;
}
