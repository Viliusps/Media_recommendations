package com.media.recommendations.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.media.recommendations.model.Recommendation;
import com.media.recommendations.model.User;


@Repository
public interface RecommendationRepository extends JpaRepository<Recommendation, Long> {
    boolean existsByFirstAndSecondAndRatingAndFirstTypeAndSecondTypeAndUser(long first, long second, boolean rating, String firstType, String secondType, User user);

    List<Recommendation> getByUser(User user);
}
