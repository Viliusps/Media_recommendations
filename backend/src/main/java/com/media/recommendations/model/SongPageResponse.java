package com.media.recommendations.model;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SongPageResponse {
    private List<Song> songs;
    private long totalSongs;
}
