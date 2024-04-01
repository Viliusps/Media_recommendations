package com.media.recommendations.model.responses;
import java.time.LocalDate;
import java.util.List;

import com.media.recommendations.model.Song;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SpotifyHistoryResponse {
    private List<Song> songs;
    private LocalDate date;
}
