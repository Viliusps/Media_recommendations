package com.media.recommendations.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.media.recommendations.model.Recommendation;
import com.media.recommendations.model.User;


@Repository
public interface RecommendationRepository extends JpaRepository<Recommendation, Long> {
    boolean existsByFirstAndSecondAndFirstTypeAndSecondType(long first, long second, String firstType, String secondType);

    List<Recommendation> getByUser(User user);
    
    @Query(value = "SELECT COUNT(*) FROM recommendations WHERE first_type = :firstType AND second_type = :secondType AND rating > 0", nativeQuery = true)
    Long countByFirstTypeAndSecondTypeAndPositive(String firstType, String secondType);

    Recommendation getByFirstAndSecondAndFirstTypeAndSecondType(long first, long second, String firstType, String secondType);

}
