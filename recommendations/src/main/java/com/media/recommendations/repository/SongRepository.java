package com.media.recommendations.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.media.recommendations.model.Song;

@Repository
public interface SongRepository extends JpaRepository<Song, Long> {
    public List<Song> findAllByOrderByIdAsc();
}
