package com.naturalia.backend.service;

import com.naturalia.backend.dto.ReviewRequest;
import com.naturalia.backend.dto.ReviewResponse;
import com.naturalia.backend.entity.Reservation;
import com.naturalia.backend.entity.Review;
import com.naturalia.backend.entity.Stay;
import com.naturalia.backend.entity.User;
import com.naturalia.backend.repository.IReservationRepository;
import com.naturalia.backend.repository.IStayRepository;
import com.naturalia.backend.repository.IReviewRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ReviewService {

    private final IReviewRepository IReviewRepository;
    private final IReservationRepository reservationRepository;
    private final IStayRepository stayRepository;

    public boolean canUserReview(User user, Long stayId) {
        List<Reservation> reservations = reservationRepository.findByUser(user);
        return reservations.stream()
                .anyMatch(r -> r.getStay().getId().equals(stayId));
    }

    public void saveReview(User user, ReviewRequest request) {
        Stay stay = stayRepository.findById(request.getStayId())
                .orElseThrow(() -> new RuntimeException("Stay not found"));

        if (!canUserReview(user, request.getStayId())) {
            throw new RuntimeException("User not allowed to review this stay");
        }

        Review review = Review.builder()
                .user(user)
                .stay(stay)
                .rating(request.getRating())
                .comment(request.getComment())
                .build();

        IReviewRepository.save(review);
    }

    public List<ReviewResponse> getReviewsForStay(Long stayId) {
        return IReviewRepository.findByStayId(stayId).stream()
                .map(r -> ReviewResponse.builder()
                        .userName(r.getUser().getFirstname() + " " + r.getUser().getLastname())
                        .rating(r.getRating())
                        .comment(r.getComment())
                        .date(r.getCreatedAt())
                        .build())
                .collect(Collectors.toList());
    }

    public double getAverageRating(Long stayId) {
        List<Review> reviews = IReviewRepository.findByStayId(stayId);
        return reviews.stream().mapToInt(Review::getRating).average().orElse(0);
    }
}
