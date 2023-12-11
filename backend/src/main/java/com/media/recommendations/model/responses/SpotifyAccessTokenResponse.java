package com.media.recommendations.model.responses;

import com.fasterxml.jackson.annotation.JsonProperty;

public class SpotifyAccessTokenResponse {

    @JsonProperty("access_token")
    private String accessToken;

    @JsonProperty("token_type")
    private String tokenType;


    public String getAccessToken() {
        return accessToken;
    }

    public String getTokenType() {
        return tokenType;
    }
}
