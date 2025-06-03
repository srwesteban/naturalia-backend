package com.naturalia.backend.controller;

import com.naturalia.backend.dto.ReservationDTO;
import com.naturalia.backend.entity.Reservation;
import com.naturalia.backend.service.IReservationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/reservations")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:5173", allowCredentials = "true")
public class ReservationController {

    private final IReservationService reservationService;

    @GetMapping
    public ResponseEntity<List<ReservationDTO>> getAllReservations() {
        List<ReservationDTO> dtos = reservationService.findAll().stream()
                .map(this::convertToDTO)
                .toList();
        return ResponseEntity.ok(dtos);
    }

    private ReservationDTO convertToDTO(Reservation reservation) {
        ReservationDTO dto = new ReservationDTO();
        dto.setId(reservation.getId());
        dto.setStayId(reservation.getStay().getId());
        dto.setUserId(reservation.getUser().getId());
        dto.setCheckIn(reservation.getCheckIn());
        dto.setCheckOut(reservation.getCheckOut());
        return dto;
    }


    @PostMapping
    public ResponseEntity<ReservationDTO> createReservation(@RequestBody ReservationDTO dto) {
        Reservation reservation = reservationService.create(dto);
        ReservationDTO response = convertToDTO(reservation);
        return ResponseEntity.ok(response);
    }


    @GetMapping("/stay/{stayId}")
    public ResponseEntity<List<ReservationDTO>> getReservationsByStay(@PathVariable Long stayId) {
        List<Reservation> reservations = reservationService.getReservationsByStayId(stayId);
        List<ReservationDTO> dtos = reservations.stream()
                .map(r -> ReservationDTO.builder()
                        .id(r.getId())
                        .stayId(r.getStay() != null ? r.getStay().getId() : null)
                        .userId(r.getUser() != null ? r.getUser().getId() : null)
                        .checkIn(r.getCheckIn())
                        .checkOut(r.getCheckOut())
                        .build())
                .toList();


        return ResponseEntity.ok(dtos);
    }


}
