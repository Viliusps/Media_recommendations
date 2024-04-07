package com.media.recommendations.model.requests;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class GetClosestSongRequest {
    private Double bpm;
    private Double averageLoudness;
    private Double dynamicComplexity;
    private Double mfccZeroMean;
    private Double bpmHistogramFirstPeakMean;
    private Double bpmHistogramFirstPeakMedian;
    private Double bpmHistogramSecondPeakMean;
    private Double bpmHistogramSecondPeakMedian;
    private Double danceability;
    private Double onsetRate;
    private String keyKey;
    private String keyScale;
    private Double tuningFrequency;
    private Double tuningEqualTemperedDeviation;
}
