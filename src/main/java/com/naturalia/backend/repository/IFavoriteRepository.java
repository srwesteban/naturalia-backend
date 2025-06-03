package com.naturalia.backend.repository;

import com.naturalia.backend.entity.Favorite;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface IFavoriteRepository extends JpaRepository<Favorite, Long> {
    List<Favorite> findByUserId(Long userId);
    Optional<Favorite> findByUserIdAndStayId(Long userId, Long stayId);
    void deleteByUserIdAndStayId(Long userId, Long stayId);
}
