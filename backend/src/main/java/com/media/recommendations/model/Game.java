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
    private String genres;

    @NotNull(message = "Rating is mandatory")
    @Column(name = "rating", nullable = false)
    private double rating;

    @NotNull(message = "Playtime is mandatory")
    @Column(name = "playtime", nullable = false)
    private int playtime;

    @NotNull(message = "Popularity is mandatory")
    @Column(name = "popularity", nullable = false)
    private int popularity;

    @NotBlank(message = "Background image is mandatory")
    @Column(name = "background_image", nullable = false)
    private String backgroundImage;

    @Column(name = "description")
    private String description;

    @Column(name = "platforms")
    private String platforms;

    @Column(name = "rawg_id")
    private Integer rawgID;

    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "game")
    private List<Comment> comments = new ArrayList<>();
}
