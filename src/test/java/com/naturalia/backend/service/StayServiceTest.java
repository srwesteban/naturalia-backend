package com.naturalia.backend.service;

import com.naturalia.backend.entity.Stay;
import com.naturalia.backend.entity.StayType;
import com.naturalia.backend.exception.ResourceNotFoundException;
import com.naturalia.backend.repository.IStayRepository;
import com.naturalia.backend.service.impl.StayService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class StayServiceTest {

    private StayService stayService;
    private IStayRepository IStayRepository;
    private final List<String> FAKELIST = new ArrayList<>();

    @BeforeEach
    void setUp() {
        IStayRepository = mock(IStayRepository.class);
        stayService = new StayService(IStayRepository);
        FAKELIST.add("Test");
    }

    @Test
    void saveStay_shouldReturnSavedStay() {
        FAKELIST.add("Test");
        Stay stay = new Stay(1L, "Cabaña Mágica", "Montaña", FAKELIST, "Prueba", 2,1, StayType.GLAMPING);
        when(IStayRepository.save(any(Stay.class))).thenReturn(stay);

        Stay result = stayService.save(stay);

        assertNotNull(result);
        assertEquals("Cabaña Mágica", result.getName());
        verify(IStayRepository, times(1)).save(stay);
    }

    @Test
    void findById_shouldReturnStayIfExists() throws Exception {
        Stay stay = new Stay(1L, "Refugio", "Bosque", FAKELIST, "Test", 1, 12, StayType.COUNTRY_HOUSE);
        when(IStayRepository.findById(1L)).thenReturn(Optional.of(stay));

        Stay result = stayService.findById(1L).orElseThrow();

        assertEquals(1L, result.getId());
        verify(IStayRepository).findById(1L);
    }

    @Test
    void findById_shouldThrowIfNotExists() {
        when(IStayRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> {
            stayService.findById(99L).orElseThrow(() -> new ResourceNotFoundException("Not found"));
        });
    }

}
