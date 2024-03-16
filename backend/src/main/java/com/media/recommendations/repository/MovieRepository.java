package com.media.recommendations.repository;

import com.media.recommendations.model.Movie;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface MovieRepository extends JpaRepository<Movie, Long> {
    public List<Movie> findAllByOrderByIdAsc();
    public Page<Movie> findAllByOrderByIdAsc(Pageable pageable);

    
    @Query
        (
            value = "SELECT * FROM movies m WHERE LOWER(m.title) LIKE LOWER(CONCAT('%', :title, '%'))",
            nativeQuery = true
        )
    List<Movie> findByTitleContaining(String title);

    boolean existsByimdbID(String imdbID);

    Movie getByimdbID(String imdbID);
}
