package com.naturalia.backend.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.naturalia.backend.dto.FeatureDTO;
import com.naturalia.backend.dto.FeatureRequest;
import com.naturalia.backend.service.IFeatureService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
public class FeatureControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private IFeatureService featureService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @DisplayName("Debería retornar todas las características")
    void shouldReturnAllFeatures() throws Exception {
        FeatureDTO f1 = new FeatureDTO(1L, "Wifi", "fa-wifi");
        FeatureDTO f2 = new FeatureDTO(2L, "Piscina", "fa-swimming-pool");

        when(featureService.getAllFeatures()).thenReturn(List.of(f1, f2));

        mockMvc.perform(get("/features"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].name").value("Wifi"))
                .andExpect(jsonPath("$[1].icon").value("fa-swimming-pool"));
    }

    @Test
    @DisplayName("Debería crear una característica")
    void shouldCreateFeature() throws Exception {
        FeatureRequest req = new FeatureRequest("Jacuzzi", "fa-hot-tub");
        FeatureDTO created = new FeatureDTO(3L, req.getName(), req.getIcon());

        when(featureService.createFeature(any())).thenReturn(created);

        mockMvc.perform(post("/features")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(req)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(3))
                .andExpect(jsonPath("$.name").value("Jacuzzi"));
    }

    @Test
    @DisplayName("Debería actualizar una característica")
    void shouldUpdateFeature() throws Exception {
        FeatureRequest req = new FeatureRequest("Parqueadero", "fa-car");
        FeatureDTO updated = new FeatureDTO(5L, req.getName(), req.getIcon());

        when(featureService.updateFeature(eq(5L), any())).thenReturn(updated);

        mockMvc.perform(put("/features/5")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(req)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Parqueadero"));
    }

    @Test
    @DisplayName("Debería eliminar una característica")
    void shouldDeleteFeature() throws Exception {
        doNothing().when(featureService).deleteFeature(6L);

        mockMvc.perform(delete("/features/6"))
                .andExpect(status().isNoContent());

        verify(featureService, times(1)).deleteFeature(6L);
    }
}
