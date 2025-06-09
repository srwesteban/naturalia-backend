package com.naturalia.backend.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ReservationResponseDTO {
    private Long reservationId;
    private String stayName;
    private String stayLocation;
    private String stayImage;
    private LocalDate checkIn;
    private LocalDate checkOut;
    private double pricePerNight;
    private long nights;
    private double totalPrice;
    private boolean emailSent;
}
