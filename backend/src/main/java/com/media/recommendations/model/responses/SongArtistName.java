package com.media.recommendations.model.responses;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SongArtistName {
    private String songName;
    private String artistName;
}
