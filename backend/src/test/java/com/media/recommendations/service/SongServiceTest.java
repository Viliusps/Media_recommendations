package com.media.recommendations.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.spy;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import com.media.recommendations.model.Song;
import com.media.recommendations.model.responses.SpotifyHistoryResponse;

@ExtendWith(MockitoExtension.class)
public class SongServiceTest {

     @InjectMocks
    private SongService songService;

    @BeforeEach
    public void setUp() {
    }

    @Test
    public void testCalculateAverage() {
        Song song1 = new Song();
        song1.setTitle("Song1");
        
        Song song2 = new Song();
        song2.setTitle("Song2");

        List<Song> songs = Arrays.asList(song1, song2);
        SpotifyHistoryResponse mockResponse = new SpotifyHistoryResponse();
        mockResponse.setSongs(songs);

        Map<String, String> mockFeatures = new HashMap<>();
        mockFeatures.put("mfccZeroMean", "0.5");
        mockFeatures.put("dynamicComplexity", "1.2");
        mockFeatures.put("averageLoudness", "-5.0");
        mockFeatures.put("onsetRate", "3.5");
        mockFeatures.put("bpmHistogramSecondPeakBpmMedian", "120");
        mockFeatures.put("bpmHistogramSecondPeakBpmMean", "118");
        mockFeatures.put("bpmHistogramFirstPeakBpmMedian", "125");
        mockFeatures.put("bpmHistogramFirstPeakBpmMean", "123");
        mockFeatures.put("bpm", "122");
        mockFeatures.put("danceability", "0.8");
        mockFeatures.put("tuningFrequency", "440.0");
        mockFeatures.put("tuningEqualTemperedDeviation", "0.1");

        SongService spySongService = spy(songService);
        doReturn(mockResponse).when(spySongService).getSpotifyHistory("testUser");
        doReturn(mockFeatures).when(spySongService).getMultipleSongsFeatures(songs);

        Song actualSong = spySongService.calculateAverage("testUser");

        assertEquals("0.5", actualSong.getMfccZeroMean());
        assertEquals("1.2", actualSong.getDynamicComplexity());
        assertEquals("-5.0", actualSong.getAverageLoudness());
        assertEquals("3.5", actualSong.getOnsetRate());
        assertEquals("120", actualSong.getBpmHistogramSecondPeakBpmMedian());
        assertEquals("118", actualSong.getBpmHistogramSecondPeakBpmMean());
        assertEquals("125", actualSong.getBpmHistogramFirstPeakBpmMedian());
        assertEquals("123", actualSong.getBpmHistogramFirstPeakBpmMean());
        assertEquals("122", actualSong.getBpm());
        assertEquals("0.8", actualSong.getDanceability());
        assertEquals("440.0", actualSong.getTuningFrequency());
        assertEquals("0.1", actualSong.getTuningEqualTemperedDeviation());
    }
}
