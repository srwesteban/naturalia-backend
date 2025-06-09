package com.naturalia.backend.controller;

import com.naturalia.backend.dto.ReservationDTO;
import com.naturalia.backend.dto.ReservationResponseDTO;
import com.naturalia.backend.entity.Reservation;
import com.naturalia.backend.entity.User;
import com.naturalia.backend.service.IAuthService;
import com.naturalia.backend.service.IEmailService;
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
    private final IAuthService authService;
    private final IEmailService iEmailService;

    @GetMapping("/mine")
    public ResponseEntity<List<ReservationDTO>> getMyReservations() {
        User user = authService.getAuthenticatedUser();
        List<Reservation> reservations = reservationService.getByUser(user);
        List<ReservationDTO> dtos = reservations.stream()
                .map(this::convertToDTO)
                .toList();
        return ResponseEntity.ok(dtos);
    }

    private ReservationDTO convertToDTO(Reservation reservation) {
        return ReservationDTO.builder()
                .id(reservation.getId())
                .stayId(reservation.getStay().getId())
                .userId(reservation.getUser().getId())
                .checkIn(reservation.getCheckIn())
                .checkOut(reservation.getCheckOut())
                .stayName(reservation.getStay().getName())
                .stayImage(reservation.getStay().getImages() != null && !reservation.getStay().getImages().isEmpty()
                        ? reservation.getStay().getImages().get(0)
                        : null)
                .stayLocation(reservation.getStay().getLocation())
                .stayPricePerNight(reservation.getStay().getPricePerNight())
                .build();
    }


    @PostMapping
    public ResponseEntity<ReservationResponseDTO> createReservation(@RequestBody ReservationDTO dto) {
        ReservationResponseDTO response = reservationService.create(dto);
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

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteReservation(@PathVariable Long id) {
        reservationService.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/test-email")
    public ResponseEntity<String> testEmail() {
        String html = """
        <h2>Prueba de envío</h2>
        <p>Este es un correo de prueba desde Naturalia</p>
    """;
        iEmailService.sendReservationConfirmation("sr.westeban@gmail.com", html);
        return ResponseEntity.ok("Correo enviado");
    }




}
