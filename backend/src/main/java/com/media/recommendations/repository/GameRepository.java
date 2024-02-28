package com.media.recommendations.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.media.recommendations.model.Game;

@Repository
public interface GameRepository extends JpaRepository<Game, Long> {
    boolean existsByName(String name);

    Game getByName(String name);
}
