package com.media.recommendations.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.media.recommendations.model.SpotifyHistory;
import com.media.recommendations.model.User;

import jakarta.transaction.Transactional;

@Repository
public interface SpotifyRepository extends JpaRepository<SpotifyHistory, Long>  {
    public List<SpotifyHistory> findAllByUserId(Long userId);

    @Transactional
    void deleteByUser(User user);
}
