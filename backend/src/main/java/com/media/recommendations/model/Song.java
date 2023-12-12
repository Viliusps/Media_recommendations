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
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "songs")
public class Song {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @NotBlank(message = "Name is mandatory")
    @Column(name = "name", nullable = false)
    private String title;

    @NotBlank(message = "Singer is mandatory")
    @Column(name = "singer", nullable = false)
    private String singer;

    @Column(name = "genre")
    private String genre;

    @NotBlank(message = "Spotify id is mandatory")
    @Column(name = "spotify_id", nullable = false)
    private String spotifyId;

    @NotBlank(message = "Image url is mandatory")
    @Column(name = "image_url")
    private String imageUrl;

    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "song")
    private List<Movie> movies = new ArrayList<>();
}