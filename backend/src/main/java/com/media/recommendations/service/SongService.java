package com.media.recommendations.service;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.media.recommendations.model.Song;
import com.media.recommendations.repository.SongRepository;

import lombok.AllArgsConstructor;

@AllArgsConstructor
@Service
public class SongService {
    private final SongRepository songRepository;

    public List<Song> getAllSongs() {
        return songRepository.findAllByOrderByIdAsc();
    }

    public Page<Song> getPageSongs(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return songRepository.findAll(pageable);
    }

     public Song getSongById(long id) {
        return songRepository.findById(id).get();
    }

    public Song createSong(Song song) {
        Song newSong = new Song();
        newSong.setGenre(song.getGenre());
        newSong.setName(song.getName());
        newSong.setSinger(song.getName());
        newSong.setSpotifyId(song.getSpotifyId());
        newSong.setMovies(song.getMovies());
        return songRepository.save(newSong);
    }

    public boolean existsSong(long id) {
        return songRepository.existsById(id);
    }

    public Song updateSong(Long id, Song song) {
        Song songFromDb = songRepository.findById(id).get();
        songFromDb.setGenre(song.getGenre());
        songFromDb.setName(song.getName());
        songFromDb.setSinger(song.getName());
        songFromDb.setSpotifyId(song.getSpotifyId());
        songFromDb.setMovies(song.getMovies());
        return songRepository.save(songFromDb);
    }

    public void deleteSong(Long id) {
        songRepository.deleteById(id);
    }
    
}
