package com.media.recommendations.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.media.recommendations.model.Game;

@Repository
public interface GameRepository extends JpaRepository<Game, Long> {
    boolean existsByName(String name);

    Game getByName(String name);

    @Query
        (
            value = "SELECT * FROM games g WHERE LOWER(g.name) LIKE LOWER(CONCAT('%', :name, '%'))",
            nativeQuery = true
        )
    List<Game> findByNameContaining(String name);
}
