package com.media.recommendations.model;

import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;
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
@Table(name = "movies")
public class Movie {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @NotNull(message = "Adult field is mandatory")
    @Column(name = "adult", nullable = false)
    private boolean adult;

    @Column(name="belongs_to_collection")
    private String belongsToCollection;

    @NotBlank(message = "Genres are mandatory")
    @Column(name="genres", nullable = false)
    private String genres;

    @Column(name = "imdb_id")
    private String imdbId;

    @Column(name = "original_language")
    private String originalLanguage;

    @NotBlank(message = "Original title is mandatory")
    @Column(name = "original_title", nullable = false)
    private String originalTitle;

    @Column(name = "overview")
    private String overview;

    @Column(name = "popularity")
    private String popularity;

    @Column(name = "production_countries")
    private String productionCountries;

    @Column(name = "release_date")
    private String releaseDate;

    @Column(name = "runtime")
    private float runtime;

    @Column(name = "spoken_languages")
    private String spokenLanguages;

    @Column(name = "title")
    private String title;

    @Column(name = "vote_average")
    private float voteAverage;

    @Column(name = "vote_count")
    private int voteCount;

    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "movie")
    private List<Comment> comments = new ArrayList<>();
}