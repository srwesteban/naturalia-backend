package com.naturalia.backend.service;

import com.naturalia.backend.dto.ReservationDTO;
import com.naturalia.backend.dto.ReservationResponseDTO;
import com.naturalia.backend.entity.Reservation;
import com.naturalia.backend.entity.Stay;
import com.naturalia.backend.entity.User;
import com.naturalia.backend.exception.ResourceNotFoundException;
import com.naturalia.backend.repository.IReservationRepository;
import com.naturalia.backend.repository.IStayRepository;
import com.naturalia.backend.repository.IUserRepository;
import com.naturalia.backend.service.impl.ReservationServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ReservationServiceImplTest {

    @Mock private IReservationRepository reservationRepository;
    @Mock private IStayRepository stayRepository;
    @Mock private IUserRepository userRepository;
    @Mock private IEmailService emailService;

    @InjectMocks
    private ReservationServiceImpl reservationService;

    private Stay stay;
    private User user;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        stay = Stay.builder()
                .id(1L)
                .name("Test Stay")
                .location("Test Location")
                .pricePerNight(100.0)
                .images(List.of("image.jpg"))
                .build();

        user = User.builder()
                .id(1L)
                .email("user@test.com")
                .build();
    }

    @Test
    void create_validReservation_savesAndSendsEmail() {
        ReservationDTO dto = new ReservationDTO();
        dto.setStayId(stay.getId());
        dto.setUserId(user.getId());
        dto.setCheckIn(LocalDate.now().plusDays(1));
        dto.setCheckOut(LocalDate.now().plusDays(3));
        dto.setStayPricePerNight(stay.getPricePerNight());
        dto.setStayName(stay.getName());
        dto.setStayLocation(stay.getLocation());
        dto.setStayImage(stay.getImages().get(0));

        when(stayRepository.findById(stay.getId())).thenReturn(Optional.of(stay));
        when(userRepository.findById(user.getId())).thenReturn(Optional.of(user));
        when(reservationRepository.findConflictingReservations(stay.getId(), dto.getCheckIn(), dto.getCheckOut()))
                .thenReturn(Collections.emptyList());
        when(reservationRepository.save(any(Reservation.class)))
                .thenAnswer(invocation -> {
                    Reservation r = invocation.getArgument(0);
                    r.setId(123L);
                    return r;
                });

        ReservationResponseDTO response = reservationService.create(dto);

        assertNotNull(response);
        assertEquals(123L, response.getReservationId());
        assertEquals(dto.getStayName(), response.getStayName());
        assertEquals(dto.getStayLocation(), response.getStayLocation());
        assertEquals(dto.getStayImage(), response.getStayImage());
        assertEquals(dto.getCheckIn(), response.getCheckIn());
        assertEquals(dto.getCheckOut(), response.getCheckOut());
        assertEquals(dto.getStayPricePerNight(), response.getPricePerNight());
        assertEquals(2, response.getNights());
        assertEquals(200.0, response.getTotalPrice());
        assertTrue(response.isEmailSent());

        verify(emailService).sendReservationConfirmation(eq(user.getEmail()), anyString());
    }

    @Test
    void create_invalidDates_throws() {
        ReservationDTO dto = new ReservationDTO();
        dto.setCheckIn(LocalDate.now().plusDays(3));
        dto.setCheckOut(LocalDate.now().plusDays(1)); // checkOut before checkIn

        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, () -> {
            reservationService.create(dto);
        });

        assertEquals("La fecha de salida debe ser posterior a la fecha de entrada.", ex.getMessage());
    }

    @Test
    void create_stayNotFound_throws() {
        ReservationDTO dto = new ReservationDTO();
        dto.setStayId(999L);
        dto.setUserId(user.getId());
        dto.setCheckIn(LocalDate.now().plusDays(1));
        dto.setCheckOut(LocalDate.now().plusDays(2));

        when(stayRepository.findById(dto.getStayId())).thenReturn(Optional.empty());

        ResourceNotFoundException ex = assertThrows(ResourceNotFoundException.class, () -> {
            reservationService.create(dto);
        });

        assertEquals("Stay not found", ex.getMessage());
    }

    @Test
    void create_userNotFound_throws() {
        ReservationDTO dto = new ReservationDTO();
        dto.setStayId(stay.getId());
        dto.setUserId(999L);
        dto.setCheckIn(LocalDate.now().plusDays(1));
        dto.setCheckOut(LocalDate.now().plusDays(2));

        when(stayRepository.findById(stay.getId())).thenReturn(Optional.of(stay));
        when(userRepository.findById(dto.getUserId())).thenReturn(Optional.empty());

        ResourceNotFoundException ex = assertThrows(ResourceNotFoundException.class, () -> {
            reservationService.create(dto);
        });

        assertEquals("User not found", ex.getMessage());
    }

    @Test
    void create_conflictingReservation_throws() {
        ReservationDTO dto = new ReservationDTO();
        dto.setStayId(stay.getId());
        dto.setUserId(user.getId());
        dto.setCheckIn(LocalDate.now().plusDays(1));
        dto.setCheckOut(LocalDate.now().plusDays(3));

        when(stayRepository.findById(stay.getId())).thenReturn(Optional.of(stay));
        when(userRepository.findById(user.getId())).thenReturn(Optional.of(user));
        when(reservationRepository.findConflictingReservations(stay.getId(), dto.getCheckIn(), dto.getCheckOut()))
                .thenReturn(List.of(new Reservation()));

        IllegalStateException ex = assertThrows(IllegalStateException.class, () -> {
            reservationService.create(dto);
        });

        assertEquals("La reserva se cruza con otra existente.", ex.getMessage());
    }

    @Test
    void findAll_returnsReservations() {
        Reservation res1 = new Reservation();
        Reservation res2 = new Reservation();

        when(reservationRepository.findAll()).thenReturn(List.of(res1, res2));

        List<Reservation> result = reservationService.findAll();

        assertEquals(2, result.size());
        verify(reservationRepository).findAll();
    }

    @Test
    void save_callsRepositorySave() {
        Reservation reservation = new Reservation();

        when(reservationRepository.save(reservation)).thenReturn(reservation);

        Reservation result = reservationService.save(reservation);

        assertEquals(reservation, result);
        verify(reservationRepository).save(reservation);
    }

    @Test
    void getReservationsByStayId_returnsList() {
        List<Reservation> list = List.of(new Reservation(), new Reservation());

        when(reservationRepository.findByStayId(1L)).thenReturn(list);

        List<Reservation> result = reservationService.getReservationsByStayId(1L);

        assertEquals(2, result.size());
        verify(reservationRepository).findByStayId(1L);
    }

    @Test
    void getByUser_returnsList() {
        List<Reservation> list = List.of(new Reservation(), new Reservation());

        when(reservationRepository.findByUser(user)).thenReturn(list);

        List<Reservation> result = reservationService.getByUser(user);

        assertEquals(2, result.size());
        verify(reservationRepository).findByUser(user);
    }

    @Test
    void deleteById_exists_deletes() {
        when(reservationRepository.existsById(1L)).thenReturn(true);
        doNothing().when(reservationRepository).deleteById(1L);

        reservationService.deleteById(1L);

        verify(reservationRepository).existsById(1L);
        verify(reservationRepository).deleteById(1L);
    }

    @Test
    void deleteById_notExists_throws() {
        when(reservationRepository.existsById(1L)).thenReturn(false);

        ResourceNotFoundException ex = assertThrows(ResourceNotFoundException.class, () -> {
            reservationService.deleteById(1L);
        });

        assertEquals("Reserva no encontrada con ID: 1", ex.getMessage());
    }
}
