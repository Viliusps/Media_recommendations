package com.media.recommendations.model;

import jakarta.persistence.Column;
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
@Table(name = "games")
public class Game {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @NotBlank(message = "Name is mandatory")
    @Column(name = "name", nullable = false)
    private String name;

    @NotBlank(message = "Release date is mandatory")
    @Column(name = "release_date", nullable = false)
    private String releaseDate;

    @NotBlank(message = "Genre is mandatory")
    @Column(name = "genre", nullable = false)
    private String genre;

    @NotNull(message = "Rating is mandatory")
    @Column(name = "rating", nullable = false)
    private double rating;

    @NotNull(message = "Playtime is mandatory")
    @Column(name = "playtime", nullable = false)
    private int playtime;
}
