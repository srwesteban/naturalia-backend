package com.naturalia.backend.service;

import com.naturalia.backend.dto.FeatureDTO;
import com.naturalia.backend.dto.FeatureRequest;
import com.naturalia.backend.entity.Feature;
import com.naturalia.backend.repository.IFeatureRepository;
import com.naturalia.backend.service.impl.FeatureServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class FeatureServiceImplTest {

    @Mock
    private IFeatureRepository featureRepository;

    @InjectMocks
    private FeatureServiceImpl featureService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void getAllFeatures_returnsDTOList() {
        Feature f1 = Feature.builder().id(1L).name("Pool").icon("pool-icon").build();
        Feature f2 = Feature.builder().id(2L).name("Wifi").icon("wifi-icon").build();

        when(featureRepository.findAll()).thenReturn(Arrays.asList(f1, f2));

        List<FeatureDTO> dtos = featureService.getAllFeatures();

        assertEquals(2, dtos.size());
        assertEquals("Pool", dtos.get(0).getName());
        assertEquals("Wifi", dtos.get(1).getName());

        verify(featureRepository).findAll();
    }

    @Test
    void createFeature_savesAndReturnsDTO() {
        FeatureRequest request = new FeatureRequest();
        request.setName("Gym");
        request.setIcon("gym-icon");

        Feature savedFeature = Feature.builder().id(10L).name("Gym").icon("gym-icon").build();

        when(featureRepository.save(any(Feature.class))).thenReturn(savedFeature);

        FeatureDTO dto = featureService.createFeature(request);

        assertNotNull(dto);
        assertEquals(10L, dto.getId());
        assertEquals("Gym", dto.getName());
        assertEquals("gym-icon", dto.getIcon());

        verify(featureRepository).save(any(Feature.class));
    }

    @Test
    void updateFeature_existingFeature_updatesAndReturnsDTO() {
        Long id = 5L;
        Feature existing = Feature.builder().id(id).name("Old Name").icon("old-icon").build();
        FeatureRequest request = new FeatureRequest();
        request.setName("New Name");
        request.setIcon("new-icon");

        Feature updatedFeature = Feature.builder().id(id).name("New Name").icon("new-icon").build();

        when(featureRepository.findById(id)).thenReturn(Optional.of(existing));
        when(featureRepository.save(existing)).thenReturn(updatedFeature);

        FeatureDTO dto = featureService.updateFeature(id, request);

        assertEquals(id, dto.getId());
        assertEquals("New Name", dto.getName());
        assertEquals("new-icon", dto.getIcon());

        verify(featureRepository).findById(id);
        verify(featureRepository).save(existing);
    }

    @Test
    void updateFeature_notFound_throws() {
        Long id = 999L;
        FeatureRequest request = new FeatureRequest();
        request.setName("Name");
        request.setIcon("icon");

        when(featureRepository.findById(id)).thenReturn(Optional.empty());

        RuntimeException ex = assertThrows(RuntimeException.class, () -> {
            featureService.updateFeature(id, request);
        });

        assertTrue(ex.getMessage().contains("Feature not found"));
        verify(featureRepository).findById(id);
        verify(featureRepository, never()).save(any());
    }

    @Test
    void deleteFeature_callsRepository() {
        Long id = 7L;

        doNothing().when(featureRepository).deleteById(id);

        featureService.deleteFeature(id);

        verify(featureRepository).deleteById(id);
    }
}
