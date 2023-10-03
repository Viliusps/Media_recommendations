package com.media.recommendations.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "comments")
public class Comment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    
    @NotNull(message = "Movie is mandatory")
    @Column(name = "movie", nullable = false)
    private long movie;

    @NotBlank(message = "Comment text is mandatory")
    @Column(name = "comment_text", nullable = false)
    private String commentText;

    @NotNull(message = "Rating is mandatory")
    @Column(name = "rating", nullable = false)
    private int rating;
}