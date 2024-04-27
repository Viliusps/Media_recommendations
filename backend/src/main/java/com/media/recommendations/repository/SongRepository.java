package com.media.recommendations.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.media.recommendations.model.Song;

@Repository
public interface SongRepository extends JpaRepository<Song, Long> {
    public List<Song> findAllByOrderByIdAsc();
    public Page<Song> findAllByOrderByIdAsc(Pageable pageable);

    @Query
        (
            value = "SELECT * FROM songs s WHERE LOWER(s.name) LIKE LOWER(CONCAT('%', :title, '%'))",
            nativeQuery = true
        )
    List<Song> findByTitleContaining(String title);

    boolean existsByisrc(String isrc);

    Song getByisrc(String isrc);

    Song getBymbid(String mbid);
}
