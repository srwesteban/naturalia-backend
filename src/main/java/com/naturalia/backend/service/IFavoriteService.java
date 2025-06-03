package com.naturalia.backend.service;

import com.naturalia.backend.dto.FavoriteDTO;
import com.naturalia.backend.entity.Favorite;

import java.util.List;

public interface IFavoriteService {
    void addFavorite(Long userId, Long stayId);
    Favorite save(Long userId, Long stayId);
    void remove(Long userId, Long stayId);
    List<FavoriteDTO> getFavoritesByUser(Long userId);
    boolean isFavorite(Long userId, Long stayId);
}
