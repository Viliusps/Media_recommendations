package com.media.recommendations.model.responses;

import java.util.List;

import com.media.recommendations.model.Song;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SongSearchResponse {
    private List<Song> songs;
    private long totalSongs;
}
