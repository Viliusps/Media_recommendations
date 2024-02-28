package com.media.recommendations.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.media.recommendations.model.Recommendation;


@Repository
public interface RecommendationRepository extends JpaRepository<Recommendation, Long> {
}
