package com.naturalia.backend.service.impl;

import com.naturalia.backend.dto.FavoriteDTO;
import com.naturalia.backend.entity.Favorite;
import com.naturalia.backend.entity.Stay;
import com.naturalia.backend.entity.User;
import com.naturalia.backend.repository.IFavoriteRepository;
import com.naturalia.backend.repository.IStayRepository;
import com.naturalia.backend.repository.IUserRepository;
import com.naturalia.backend.service.IFavoriteService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class FavoriteServiceImpl implements IFavoriteService {

    private final IFavoriteRepository favoriteRepository;
    private final IUserRepository userRepository;
    private final IStayRepository stayRepository;

    @Override
    public Favorite save(Long userId, Long stayId) {
        User user = userRepository.findById(userId).orElseThrow();
        Stay stay = stayRepository.findById(stayId).orElseThrow();

        if (favoriteRepository.findByUserIdAndStayId(userId, stayId).isEmpty()) {
            return favoriteRepository.save(Favorite.builder()
                    .user(user)
                    .stay(stay)
                    .build());
        }

        return null;
    }

    @Override
    @Transactional
    public void remove(Long userId, Long stayId) {
        favoriteRepository.deleteByUserIdAndStayId(userId, stayId);
    }

    @Override
    public List<FavoriteDTO> getFavoritesByUser(Long userId) {
        List<Favorite> favorites = favoriteRepository.findByUserId(userId);

        return favorites.stream().map(fav -> FavoriteDTO.builder()
                .id(fav.getId())
                .userId(fav.getUser().getId())
                .stayId(fav.getStay().getId())
                .stayName(fav.getStay().getName())
                .stayImage(fav.getStay().getImages().isEmpty() ? null : fav.getStay().getImages().get(0))
                .location(fav.getStay().getLocation())
                .build()
        ).toList();
    }


    @Override
    public boolean isFavorite(Long userId, Long stayId) {
        return favoriteRepository.findByUserIdAndStayId(userId, stayId).isPresent();
    }

    @Override
    public void addFavorite(Long userId, Long stayId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        Stay stay = stayRepository.findById(stayId)
                .orElseThrow(() -> new RuntimeException("Alojamiento no encontrado"));

        Favorite favorite = Favorite.builder()
                .user(user)
                .stay(stay)
                .build();

        favoriteRepository.save(favorite);
    }

}
