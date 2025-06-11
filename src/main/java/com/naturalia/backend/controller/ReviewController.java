package com.naturalia.backend.controller;

import com.naturalia.backend.dto.ReviewRequest;
import com.naturalia.backend.dto.ReviewResponse;
import com.naturalia.backend.entity.User;
import com.naturalia.backend.service.IAuthService;
import com.naturalia.backend.service.ReviewService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/reviews")
@RequiredArgsConstructor
public class ReviewController {

    private final ReviewService reviewService;
    private final IAuthService authService;

    // Obtener reseñas de un alojamiento
    @GetMapping("/stay/{stayId}")
    public ResponseEntity<List<ReviewResponse>> getReviews(@PathVariable Long stayId) {
        return ResponseEntity.ok(reviewService.getReviewsForStay(stayId));
    }

    // Obtener promedio de puntuación
    @GetMapping("/stay/{stayId}/average")
    public ResponseEntity<Double> getAverageRating(@PathVariable Long stayId) {
        return ResponseEntity.ok(reviewService.getAverageRating(stayId));
    }

    // Crear una nueva reseña (requiere autenticación)
    @PostMapping
    public ResponseEntity<String> createReview(@Valid @RequestBody ReviewRequest reviewRequest) {
        User user = authService.getAuthenticatedUser();
        reviewService.saveReview(user, reviewRequest);
        return ResponseEntity.ok("Review saved successfully");
    }


}
