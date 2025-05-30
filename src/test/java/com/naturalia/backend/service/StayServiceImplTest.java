package com.naturalia.backend.service;

import com.naturalia.backend.entity.Stay;
import com.naturalia.backend.exception.ResourceNotFoundException;
import com.naturalia.backend.repository.*;
import com.naturalia.backend.service.impl.StayServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class StayServiceImplTest {

    private StayServiceImpl stayServiceImpl;

    private IStayRepository stayRepository;
    private IFeatureRepository featureRepository;
    private ICategoryRepository categoryRepository;
    private IUserRepository userRepository;

    private List<String> fakeImageList;

    @BeforeEach
    void setUp() {
        stayRepository = mock(IStayRepository.class);
        featureRepository = mock(IFeatureRepository.class);
        categoryRepository = mock(ICategoryRepository.class);
        userRepository = mock(IUserRepository.class);

        stayServiceImpl = new StayServiceImpl(
                stayRepository,
                featureRepository,
                categoryRepository,
                userRepository
        );

        fakeImageList = List.of("https://image1.jpg", "https://image2.jpg");
    }

    @Test
    void saveStay_shouldReturnSavedStay() {
        Stay stay = Stay.builder()
                .id(1L)
                .name("Cabaña Mágica")
                .description("Lugar encantado")
                .images(fakeImageList)
                .location("Montaña")
                .capacity(4)
                .pricePerNight(120)
                .bedrooms(2)
                .beds(3)
                .bathrooms(1)
                .latitude(5.123)
                .longitude(-74.321)
                .build();

        when(stayRepository.save(any(Stay.class))).thenReturn(stay);

        Stay result = stayServiceImpl.save(stay);

        assertNotNull(result);
        assertEquals("Cabaña Mágica", result.getName());
        verify(stayRepository, times(1)).save(stay);
    }

    @Test
    void findById_shouldReturnStayIfExists() {
        Stay stay = Stay.builder()
                .id(2L)
                .name("Refugio Natural")
                .description("En el bosque")
                .images(fakeImageList)
                .location("Bosque Andino")
                .capacity(2)
                .pricePerNight(90)
                .bedrooms(1)
                .beds(1)
                .bathrooms(1)
                .latitude(4.987)
                .longitude(-75.123)
                .build();

        when(stayRepository.findById(2L)).thenReturn(Optional.of(stay));

        Stay result = stayServiceImpl.findById(2L).orElseThrow();

        assertEquals("Refugio Natural", result.getName());
        verify(stayRepository).findById(2L);
    }

    @Test
    void findById_shouldThrowIfNotExists() {
        when(stayRepository.findById(999L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> {
            stayServiceImpl.findById(999L).orElseThrow(() -> new ResourceNotFoundException("Not found"));
        });
    }
}
