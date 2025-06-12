package com.naturalia.backend.service;

import com.naturalia.backend.dto.*;
import com.naturalia.backend.entity.*;
import com.naturalia.backend.exception.DuplicateNameException;
import com.naturalia.backend.exception.ResourceNotFoundException;
import com.naturalia.backend.mapper.StayMapper;
import com.naturalia.backend.repository.*;
import com.naturalia.backend.service.impl.StayServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;

import java.time.LocalDate;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class StayServiceImplTest {

    @Mock
    private IStayRepository stayRepository;

    @Mock
    private IFeatureRepository featureRepository;

    @Mock
    private ICategoryRepository categoryRepository;

    @Mock
    private IUserRepository userRepository;

    @Mock
    private StayMapper stayMapper;

    @InjectMocks
    private StayServiceImpl stayService;

    private User host;
    private Stay stay;
    private StayDTO stayDTO;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        host = User.builder().id(1L).firstname("Host").build();

        stay = Stay.builder()
                .id(1L)
                .name("Test Stay")
                .description("Desc")
                .location("Loc")
                .pricePerNight(100.0)
                .capacity(4)
                .bedrooms(2)
                .beds(3)
                .bathrooms(1)
                .latitude(12.3)
                .longitude(45.6)
                .features(Collections.emptyList())
                .categories(Collections.emptyList())
                .host(host)
                .build();

        stayDTO = StayDTO.builder()
                .id(stay.getId())
                .name(stay.getName())
                .build();
    }

    @Test
    void save_duplicateName_throws() {
        when(stayRepository.existsByName(stay.getName())).thenReturn(true);

        DuplicateNameException ex = assertThrows(DuplicateNameException.class, () -> {
            stayService.save(stay);
        });

        assertEquals("DUPLICATE_NAME", ex.getMessage());
        verify(stayRepository, never()).save(any());
    }

    @Test
    void save_success() {
        when(stayRepository.existsByName(stay.getName())).thenReturn(false);
        when(stayRepository.save(stay)).thenReturn(stay);

        Stay saved = stayService.save(stay);

        assertEquals(stay, saved);
        verify(stayRepository).save(stay);
    }

    @Test
    void findById_found() {
        when(stayRepository.findById(1L)).thenReturn(Optional.of(stay));

        Optional<Stay> result = stayService.findById(1L);

        assertTrue(result.isPresent());
        assertEquals(stay, result.get());
    }

    @Test
    void update_nonExisting_throws() {
        when(stayRepository.existsById(1L)).thenReturn(false);

        ResourceNotFoundException ex = assertThrows(ResourceNotFoundException.class, () -> {
            stayService.update(stay);
        });

        assertTrue(ex.getMessage().contains("Stay not found"));
        verify(stayRepository, never()).save(any());
    }

    @Test
    void update_existing_success() {
        when(stayRepository.existsById(stay.getId())).thenReturn(true);
        when(stayRepository.save(stay)).thenReturn(stay);

        stayService.update(stay);

        verify(stayRepository).save(stay);
    }

    @Test
    void delete_nonExisting_throws() {
        when(stayRepository.existsById(1L)).thenReturn(false);

        ResourceNotFoundException ex = assertThrows(ResourceNotFoundException.class, () -> {
            stayService.delete(1L);
        });

        assertTrue(ex.getMessage().contains("Stay not found"));
        verify(stayRepository, never()).deleteById(any());
    }

    @Test
    void delete_existing_success() {
        when(stayRepository.existsById(1L)).thenReturn(true);
        doNothing().when(stayRepository).deleteById(1L);

        stayService.delete(1L);

        verify(stayRepository).deleteById(1L);
    }

    @Test
    void create_withHostId_success() {
        StayRequest req = new StayRequest();
        req.setName("New Stay");
        req.setHostId(host.getId());
        req.setFeatureIds(Collections.emptyList());
        req.setCategoryIds(Collections.emptyList());

        when(userRepository.findById(host.getId())).thenReturn(Optional.of(host));
        when(featureRepository.findAllById(any())).thenReturn(Collections.emptyList());
        when(categoryRepository.findAllById(any())).thenReturn(Collections.emptyList());
        when(stayRepository.save(any())).thenReturn(stay);

        StayDTO dto = stayService.create(req);

        assertNotNull(dto);
        verify(userRepository).findById(host.getId());
        verify(stayRepository).save(any());
    }

    @Test
    void create_withoutHostId_usesAuthUser() {
        StayRequest req = new StayRequest();
        req.setName("New Stay");
        req.setFeatureIds(Collections.emptyList());
        req.setCategoryIds(Collections.emptyList());

        // Mock SecurityContext
        var authentication = mock(org.springframework.security.core.Authentication.class);
        when(authentication.getName()).thenReturn(host.getFirstname()); // or email as expected

        var securityContext = mock(org.springframework.security.core.context.SecurityContext.class);
        when(securityContext.getAuthentication()).thenReturn(authentication);
        org.springframework.security.core.context.SecurityContextHolder.setContext(securityContext);

        when(userRepository.findByEmail(host.getFirstname())).thenReturn(Optional.of(host));
        when(featureRepository.findAllById(any())).thenReturn(Collections.emptyList());
        when(categoryRepository.findAllById(any())).thenReturn(Collections.emptyList());
        when(stayRepository.save(any())).thenReturn(stay);

        StayDTO dto = stayService.create(req);

        assertNotNull(dto);
        verify(userRepository).findByEmail(host.getFirstname());
        verify(stayRepository).save(any());
    }

    @Test
    void updateStay_existing_success() {
        StayRequest req = new StayRequest();
        req.setName("Updated Stay");
        req.setFeatureIds(Collections.emptyList());
        req.setCategoryIds(Collections.emptyList());

        when(stayRepository.findById(stay.getId())).thenReturn(Optional.of(stay));
        when(featureRepository.findAllById(any())).thenReturn(Collections.emptyList());
        when(categoryRepository.findAllById(any())).thenReturn(Collections.emptyList());
        when(stayRepository.save(any())).thenReturn(stay);

        StayDTO dto = stayService.updateStay(stay.getId(), req);

        assertNotNull(dto);
        assertEquals(stay.getId(), dto.getId());
        verify(stayRepository).save(any());
    }

    @Test
    void updateStay_notFound_throws() {
        StayRequest req = new StayRequest();
        when(stayRepository.findById(999L)).thenReturn(Optional.empty());

        ResourceNotFoundException ex = assertThrows(ResourceNotFoundException.class, () -> {
            stayService.updateStay(999L, req);
        });

        assertTrue(ex.getMessage().contains("Stay not found"));
    }




    @Test
    void findDTOById_notFound_throws() {
        when(stayRepository.findById(999L)).thenReturn(Optional.empty());

        ResourceNotFoundException ex = assertThrows(ResourceNotFoundException.class, () -> {
            stayService.findDTOById(999L);
        });

        assertTrue(ex.getMessage().contains("Stay not found"));
    }

    @Test
    void findAvailableStays_returnsList() {
        List<Stay> stays = List.of(stay);

        when(stayRepository.findAvailableStays(any(), any())).thenReturn(stays);

        List<Stay> result = stayService.findAvailableStays(LocalDate.now(), LocalDate.now().plusDays(1));

        assertEquals(1, result.size());
    }

    @Test
    void getSuggestionsByName_returnsDTOList() {
        when(stayRepository.findByNameContainingIgnoreCase("query")).thenReturn(List.of(stay));
        when(stayMapper.toDTO(stay)).thenReturn(stayDTO);

        List<StayDTO> result = stayService.getSuggestionsByName("query");

        assertEquals(1, result.size());
    }


}
