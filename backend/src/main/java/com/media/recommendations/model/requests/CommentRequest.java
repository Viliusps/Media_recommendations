package com.media.recommendations.model.requests;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CommentRequest {
    private Long movie;
    private String commentText;
    private int rating;
    private Long song;
    private Long game;
    private String username;
}
