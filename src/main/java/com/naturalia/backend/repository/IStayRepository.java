package com.naturalia.backend.repository;

import com.naturalia.backend.entity.Stay;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface IStayRepository extends JpaRepository<Stay, Long> {

    Optional<Stay> findByName(String name);

    boolean existsByName(String name);

    @Query("""
                SELECT s FROM Stay s 
                WHERE LOWER(s.location) LIKE LOWER(CONCAT('%', :location, '%'))
                AND s.id NOT IN (
                    SELECT r.stay.id FROM Reservation r
                    WHERE r.checkIn <= :endDate AND r.checkOut >= :startDate
                )
            """)
    List<Stay> findAvailableStays(@Param("location") String location,
                                  @Param("startDate") LocalDate startDate,
                                  @Param("endDate") LocalDate endDate);

    List<Stay> findByLocationContainingIgnoreCase(String location);


    @Query("""
                SELECT s FROM Stay s
                WHERE s.id NOT IN (
                    SELECT r.stay.id FROM Reservation r
                    WHERE r.checkIn < :checkOut AND r.checkOut > :checkIn
                )
            """)
    List<Stay> findAvailableStays(
            @Param("checkIn") LocalDate checkIn,
            @Param("checkOut") LocalDate checkOut
    );

    @Query("""
                SELECT s FROM Stay s
                WHERE LOWER(s.name) LIKE LOWER(CONCAT('%', :query, '%'))
            """)
    List<Stay> findByNameContainingIgnoreCase(@Param("query") String query);


}
