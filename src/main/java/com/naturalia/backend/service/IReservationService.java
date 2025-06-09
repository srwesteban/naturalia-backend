package com.naturalia.backend.service;

import com.naturalia.backend.dto.ReservationDTO;
import com.naturalia.backend.dto.ReservationResponseDTO;
import com.naturalia.backend.entity.Reservation;
import com.naturalia.backend.entity.User;

import java.util.List;

public interface IReservationService {
    List<Reservation> findAll();
    Reservation save(Reservation reservation);
    ReservationResponseDTO create(ReservationDTO dto);
    List<Reservation> getReservationsByStayId(Long stayId);
    List<Reservation> getByUser(User user);
    void deleteById(Long id);




}
