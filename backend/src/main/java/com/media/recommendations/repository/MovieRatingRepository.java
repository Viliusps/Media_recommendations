package com.media.recommendations.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.media.recommendations.model.MovieRating;

@Repository
public interface MovieRatingRepository extends JpaRepository<MovieRating, Long> {
}
