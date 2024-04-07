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

    @Column(name = "mfcc_zero_mean")
    private String mfccZeroMean;

    @Column(name = "dynamic_complexity")
    private String dynamicComplexity;

    @Column(name = "average_loudness")
    private String averageLoudness;

    @Column(name = "onset_rate")
    private String onsetRate;

    @Column(name = "bpm_histogram_second_peak_bpm_median")
    private String bpmHistogramSecondPeakBpmMedian;

    @Column(name = "bpm_histogram_second_peak_bpm_mean")
    private String bpmHistogramSecondPeakBpmMean;

    @Column(name = "bpm_histogram_first_peak_bpm_median")
    private String bpmHistogramFirstPeakBpmMedian;

    @Column(name = "bpm_histogram_first_peak_bpm_mean")
    private String bpmHistogramFirstPeakBpmMean;

    @Column(name = "bpm")
    private String bpm;

    @Column(name = "danceability")
    private String danceability;

    @Column(name = "tuning_frequency")
    private String tuningFrequency;

    @Column(name = "tuning_equal_tempered_deviation")
    private String tuningEqualTemperedDeviation;

    @Column(name = "key_scale")
    private String keyScale;

    @Column(name = "key_key")
    private String keyKey;


    @NotNull(message = "Popularity is mandatory")
    @Column(name = "popularity", nullable = false)
    private int popularity;

    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "song")
    private List<Comment> comments = new ArrayList<>();
}