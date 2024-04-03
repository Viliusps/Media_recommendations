package com.media.recommendations.model.requests;

import lombok.Data;

@Data
public class SpotifyUserSongsRequest {
    private String token;
    private String username;
}
