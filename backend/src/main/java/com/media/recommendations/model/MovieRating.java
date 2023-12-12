package com.media.recommendations.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
@Entity
@Table(name = "movie_rating")
public class MovieRating {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @NotNull(message = "Movie is mandatory")
    @Column(name = "movie", nullable = false)
    private long movie;

    @Column(name = "source")
    @JsonProperty("Source")
    private String source;

    @Column(name = "value")
    @JsonProperty("Value")
    private String value;
}
