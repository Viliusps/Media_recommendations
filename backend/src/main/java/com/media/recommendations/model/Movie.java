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
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonProperty;

import jakarta.persistence.Table;
import lombok.Data;

@Data
@Entity
@Table(name = "movies")
public class Movie {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    
    @NotBlank
    @JsonProperty("Title")
    @Column(name = "title")
    private String title;

    @NotBlank
    @JsonProperty("Year")
    @Column(name = "year")
    private String year;

    @NotBlank
    @JsonProperty("Rated")
    @Column(name = "rated")
    private String rated;

    @JsonProperty("Released")
    @Column(name = "released")
    private String released;

    @JsonProperty("Runtime")
    @Column(name = "runtime")
    private String runtime;

    @JsonProperty("Genre")
    @Column(name = "genre")
    private String genre;

    @JsonProperty("Director")
    @Column(name = "director")
    private String director;

    @JsonProperty("Writer")
    @Column(name = "writer")
    private String writer;

    @JsonProperty("Actors")
    @Column(name = "actors")
    private String actors;

    @JsonProperty("Plot")
    @Column(name = "plot", length = 1000)
    private String plot;

    @JsonProperty("Language")
    @Column(name = "language")
    private String language;

    @JsonProperty("Country")
    @Column(name = "country")
    private String country;

    @JsonProperty("Awards")
    @Column(name = "awards")
    private String awards;

    @JsonProperty("Poster")
    @Column(name = "poster")
    private String poster;

    @JsonProperty("Metascore")
    @Column(name = "metascore")
    private String metascore;

    @JsonProperty("imdbRating")
    @Column(name = "imdb_rating")
    private String imdbRating;

    @JsonProperty("imdbVotes")
    @Column(name = "imdb_votes")
    private String imdbVotes;

    @NotNull
    @JsonProperty("imdbID")
    @Column(name = "imdb_id")
    private String imdbID;

    @JsonProperty("Type")
    @Column(name = "type")
    private String type;

    @JsonProperty("DVD")
    @Column(name = "dvd")
    private String dvd;

    @JsonProperty("BoxOffice")
    @Column(name = "box_office")
    private String boxOffice;

    @JsonProperty("Production")
    @Column(name = "production")
    private String production;

    @JsonProperty("Website")
    @Column(name = "website")
    private String website;

    @NotBlank
    @JsonProperty("Response")
    @Column(name = "response")
    private String response;

    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "movie")
    private List<Comment> comments = new ArrayList<>();

    @NotNull(message = "Popularity is mandatory")
    @Column(name = "popularity", nullable = false)
    private int popularity;
}
