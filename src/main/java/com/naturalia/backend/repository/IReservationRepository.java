package com.naturalia.backend.repository;

import com.naturalia.backend.entity.Reservation;
import com.naturalia.backend.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;

public interface IReservationRepository extends JpaRepository<Reservation, Long> {

    @Query("""
                SELECT r FROM Reservation r
                WHERE r.stay.id = :stayId
                AND r.checkIn < :checkOut
                AND r.checkOut > :checkIn
            """)
    List<Reservation> findConflictingReservations(
            @Param("stayId") Long stayId,
            @Param("checkIn") LocalDate checkIn,
            @Param("checkOut") LocalDate checkOut
    );

    @Query("""
                SELECT r FROM Reservation r
                WHERE r.stay.id = :stayId
            """)
    List<Reservation> findByStayId(@Param("stayId") Long stayId);

    List<Reservation> findByUser(User user);


}
