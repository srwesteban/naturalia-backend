package com.naturalia.backend.service.impl;

import com.naturalia.backend.dto.ReservationDTO;
import com.naturalia.backend.entity.Reservation;
import com.naturalia.backend.entity.Stay;
import com.naturalia.backend.entity.User;
import com.naturalia.backend.exception.ResourceNotFoundException;
import com.naturalia.backend.repository.IReservationRepository;
import com.naturalia.backend.repository.IStayRepository;
import com.naturalia.backend.repository.IUserRepository;
import com.naturalia.backend.service.IReservationService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ReservationServiceImpl implements IReservationService {

    private final IReservationRepository reservationRepository;
    private final IStayRepository stayRepository;
    private final IUserRepository userRepository;

    public Reservation create(ReservationDTO dto) {
        if (!dto.getCheckOut().isAfter(dto.getCheckIn())) {
            throw new IllegalArgumentException("La fecha de salida debe ser posterior a la fecha de entrada.");
        }

        Stay stay = stayRepository.findById(dto.getStayId())
                .orElseThrow(() -> new ResourceNotFoundException("Stay not found"));

        User user = userRepository.findById(dto.getUserId())
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        List<Reservation> conflicts = reservationRepository.findConflictingReservations(
                dto.getStayId(), dto.getCheckIn(), dto.getCheckOut());

        if (!conflicts.isEmpty()) {
            throw new IllegalStateException("La reserva se cruza con otra existente.");
        }

        Reservation reservation = Reservation.builder()
                .stay(stay)
                .user(user)
                .checkIn(dto.getCheckIn())
                .checkOut(dto.getCheckOut())
                .build();

        return reservationRepository.save(reservation);
    }




    @Override
    public List<Reservation> findAll() {
        return reservationRepository.findAll();
    }

    @Override
    public Reservation save(Reservation reservation) {
        return reservationRepository.save(reservation);
    }


    @Override
    public List<Reservation> getReservationsByStayId(Long stayId) {
        return reservationRepository.findByStayId(stayId);
    }

}
