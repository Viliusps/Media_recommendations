package com.media.recommendations.repository;

import com.media.recommendations.model.Movie;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MovieRepository extends JpaRepository<Movie, Long> {
    public List<Movie> findAllByOrderByIdAsc();
    public Page<Movie> findAllByOrderByIdAsc(Pageable pageable);
}
