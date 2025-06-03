package com.naturalia.backend.controller;

import com.naturalia.backend.dto.FavoriteDTO;
import com.naturalia.backend.entity.Favorite;
import com.naturalia.backend.request.FavoriteRequest;
import com.naturalia.backend.service.IFavoriteService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/favorites")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:5173", allowCredentials = "true")
public class FavoriteController {

    private final IFavoriteService favoriteService;

    @PostMapping
    public ResponseEntity<FavoriteDTO> addFavorite(@RequestBody FavoriteRequest request) {
        Favorite saved = favoriteService.save(request.getUserId(), request.getStayId());

        FavoriteDTO dto = FavoriteDTO.builder()
                .id(saved.getId())
                .userId(saved.getUser().getId())
                .stayId(saved.getStay().getId())
                .stayName(saved.getStay().getName())
                .stayImage(saved.getStay().getImages().isEmpty() ? null : saved.getStay().getImages().get(0))
                .location(saved.getStay().getLocation())
                .build();

        return ResponseEntity.ok(dto);
    }




    @DeleteMapping
    public ResponseEntity<Void> removeFavorite(@RequestParam Long userId, @RequestParam Long stayId) {
        favoriteService.remove(userId, stayId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<FavoriteDTO>> getUserFavorites(@PathVariable Long userId) {
        return ResponseEntity.ok(favoriteService.getFavoritesByUser(userId));
    }


    @GetMapping("/check")
    public ResponseEntity<Boolean> isFavorite(
            @RequestParam Long userId,
            @RequestParam Long stayId
    ) {
        return ResponseEntity.ok(favoriteService.isFavorite(userId, stayId));
    }
}
