package com.naturalia.backend.service;

import com.naturalia.backend.dto.ReservationDTO;
import com.naturalia.backend.entity.Reservation;

import java.util.List;

public interface IReservationService {
    List<Reservation> findAll();
    Reservation save(Reservation reservation);
    Reservation create(ReservationDTO dto);
    List<Reservation> getReservationsByStayId(Long stayId);


}
