package com.media.recommendations.controller;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.media.recommendations.model.Song;
import com.media.recommendations.model.requests.GetClosestSongRequest;
import com.media.recommendations.model.requests.GetSpotifyHistoryRequest;
import com.media.recommendations.model.requests.NameRequest;
import com.media.recommendations.model.requests.SongSearchRequest;
import com.media.recommendations.model.requests.SpotifyUserSongsRequest;
import com.media.recommendations.model.responses.SongPageResponse;
import com.media.recommendations.model.responses.SpotifyHistoryResponse;
import com.media.recommendations.service.SongService;


@AllArgsConstructor
@CrossOrigin
@RestController
@RequestMapping("/api/v1/songs")
public class SongController {
    SongService songService;

    @GetMapping
    public ResponseEntity<List<Song>> getAllSongs() {
        return new ResponseEntity<>(songService.getAllSongs(), HttpStatus.OK);
    }

    @GetMapping("/page")
    public ResponseEntity<SongPageResponse> getPageSongs(@RequestParam(defaultValue = "0") Integer page,
            @RequestParam(defaultValue = "10") Integer size) {
        return new ResponseEntity<>(songService.getPageSongs(page, size), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Song> getSongById(@PathVariable long id) {
        if (songService.existsSong(id)) {
            return new ResponseEntity<>(songService.getSongById(id), HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @PostMapping
    public ResponseEntity<Song> createSong(@Valid @RequestBody Song song) {
        return new ResponseEntity<>(songService.createSong(song), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Song> updateSong(@PathVariable long id, @Valid @RequestBody Song song) {
        if (songService.existsSong(id)) {
            return new ResponseEntity<>(songService.updateSong(id, song), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping({"/{id}"})
    public ResponseEntity<Song> deleteSong(@PathVariable Long id) {
        if (songService.existsSong(id)) {
            songService.deleteSong(id);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @PostMapping("/spotify")
    public ResponseEntity<List<Song>> getUserSongs(@RequestBody SpotifyUserSongsRequest request) {
        List<Song> response = songService.getUserSongs(request.getToken(), request.getUsername());
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PostMapping("/search")
    public ResponseEntity<SongPageResponse> searchSongs(@RequestBody @Valid SongSearchRequest request) {
        SongPageResponse songs = songService.search(request.getSearch());
        return new ResponseEntity<>(songs, HttpStatus.OK);
    }

    @PostMapping("/check")
    public ResponseEntity<Boolean> checkIfSongExists(@RequestBody NameRequest request) {
        Boolean result = songService.checkIfSongExists(request.getName());
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @PostMapping("/isrc")
    public ResponseEntity<String> getSongISRC(@RequestBody NameRequest request) {
        String result = songService.getISRCBySongName(request.getName());
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @PostMapping("/search-closest")
    public ResponseEntity<String> searchClosestSongFeature(@RequestBody GetClosestSongRequest request) {
        String result = songService.getClosestSongFromFeatures(request.getBpm(), request.getAverageLoudness(), request.getDynamicComplexity(), request.getMfccZeroMean(), request.getBpmHistogramFirstPeakMean(), request.getBpmHistogramFirstPeakMedian(), request.getBpmHistogramSecondPeakMean(), request.getBpmHistogramSecondPeakMedian(), request.getDanceability(), request.getOnsetRate(), request.getTuningFrequency(), request.getTuningEqualTemperedDeviation());
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @PostMapping("/spotify-history")
    public ResponseEntity<SpotifyHistoryResponse> getSpotifyHistory(@RequestBody GetSpotifyHistoryRequest request) {
        SpotifyHistoryResponse response = songService.getSpotifyHistory(request.getUsername());
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}
