package com.naturalia.backend.repository;

import com.naturalia.backend.entity.Reservation;
import com.naturalia.backend.entity.Review;
import com.naturalia.backend.entity.Stay;
import com.naturalia.backend.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface IReviewRepository extends JpaRepository<Review, Long> {
    List<Review> findByStayId(Long stayId);
    Optional<Review> findByUserAndStay(User user, Stay stay);
    int countByStayId(Long stayId);
    List<Reservation> findByUser(User user);

}
