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

    @NotBlank(message = "Spotify id is mandatory")
    @Column(name = "spotify_id", nullable = false)
    private String spotifyId;

    @NotBlank(message = "Image url is mandatory")
    @Column(name = "image_url")
    private String imageUrl;

    @Column(name = "isrc")
    private String isrc;

    @Column(name = "chords_changes_rate")
    private String chordsChangesRate;

    @Column(name = "key_strength")
    private String keyStrength;

    @Column(name = "danceability")
    private String danceability;

    @Column(name = "bpm")
    private String bpm;

    @Column(name = "beats_loudness")
    private String beatsLoudness;

    @Column(name = "beats_count")
    private String beatsCount;

    @Column(name = "spectral_energy")
    private String spectralEnergy;

    @Column(name = "silence_rate")
    private String silenceRate;

    @Column(name = "dissonance")
    private String dissonance;

    @Column(name = "average_loudness")
    private String averageLoudness;

    @Column(name = "dynamic_complexity")
    private String dynamicComplexity;

    @Column(name = "pitch_salience")
    private String pitchSalience;

    @NotNull(message = "Popularity is mandatory")
    @Column(name = "popularity", nullable = false)
    private int popularity;
}