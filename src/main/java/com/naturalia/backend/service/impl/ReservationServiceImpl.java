package com.naturalia.backend.service.impl;

import com.naturalia.backend.dto.ReservationDTO;
import com.naturalia.backend.dto.ReservationResponseDTO;
import com.naturalia.backend.entity.Reservation;
import com.naturalia.backend.entity.Stay;
import com.naturalia.backend.entity.User;
import com.naturalia.backend.exception.ResourceNotFoundException;
import com.naturalia.backend.repository.IReservationRepository;
import com.naturalia.backend.repository.IStayRepository;
import com.naturalia.backend.repository.IUserRepository;
import com.naturalia.backend.service.IEmailService;
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
    private final IEmailService emailService;


    @Override
    public ReservationResponseDTO create(ReservationDTO dto) {
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

        Reservation saved = reservationRepository.save(reservation);

        // ✅ Calcular precio total
        long days = java.time.temporal.ChronoUnit.DAYS.between(dto.getCheckIn(), dto.getCheckOut());
        double totalPrice = days * dto.getStayPricePerNight();

        String content = """
                    <div style="font-family: Arial, sans-serif; color: #333;">
                        <h2 style="color: #4CAF50;">¡Gracias por tu reserva en Naturalia!</h2>
                        <img src="%s" alt="Imagen del alojamiento" style="width:100%%; max-width:400px; border-radius:10px; margin-top:10px;" />
                        <p><strong>Producto reservado:</strong> %s</p>
                        <p><strong>Ubicación:</strong> %s</p>
                        <p><strong>Desde:</strong> %s</p>
                        <p><strong>Hasta:</strong> %s</p>
                        <p><strong>Precio por noche:</strong> %s</p>
                        <p><strong>Total (%d noche%s):</strong> %s</p>
                        <br/>
                        <p>Gracias por confiar en Naturalia. Te esperamos.</p>
                    </div>
                """.formatted(
                (stay.getImages() != null && !stay.getImages().isEmpty() ? stay.getImages().get(0) : ""),
                stay.getName(),
                stay.getLocation(),
                dto.getCheckIn(),
                dto.getCheckOut(),
                String.format("$%,.0f", stay.getPricePerNight()),
                days,
                days == 1 ? "" : "s",
                String.format("$%,.0f", totalPrice)
        );

        emailService.sendReservationConfirmation(user.getEmail(), content);

        return ReservationResponseDTO.builder()
                .reservationId(saved.getId())
                .stayName(dto.getStayName())
                .stayLocation(dto.getStayLocation())
                .stayImage(dto.getStayImage())
                .checkIn(dto.getCheckIn())
                .checkOut(dto.getCheckOut())
                .pricePerNight(dto.getStayPricePerNight())
                .nights(days)
                .totalPrice(totalPrice)
                .emailSent(true)
                .build();
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

    @Override
    public List<Reservation> getByUser(User user) {
        return reservationRepository.findByUser(user);
    }

    @Override
    public void deleteById(Long id) {
        reservationRepository.deleteById(id);
    }


}
