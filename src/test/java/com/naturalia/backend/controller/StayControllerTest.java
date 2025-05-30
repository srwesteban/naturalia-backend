package com.naturalia.backend.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.naturalia.backend.dto.StayDTO;
import com.naturalia.backend.dto.StayRequest;
import com.naturalia.backend.service.IStayService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
public class StayControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private IStayService stayService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @DisplayName("Debería crear un stay")
    void shouldCreateStay() throws Exception {
        StayRequest request = new StayRequest();
        StayDTO created = new StayDTO(1L, "Refugio del Bosque", "Descripción", List.of("img.jpg"), "Bosque", 2, 100.0, 1, 1, 1, 4.5, -74.2, List.of(), List.of());

        when(stayService.create(any())).thenReturn(created);

        mockMvc.perform(post("/stays")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("Refugio del Bosque"));
    }

    @Test
    @DisplayName("Debería retornar todos los stays")
    void shouldReturnAllStays() throws Exception {
        StayDTO s1 = new StayDTO(1L, "Cabaña", "Desc", List.of("img1.jpg"), "Montaña", 2, 120.0, 1, 1, 1, 4.5, -74.0, List.of(), List.of());
        StayDTO s2 = new StayDTO(2L, "Casa Campestre", "Desc", List.of("img2.jpg"), "Campo", 4, 200.0, 2, 2, 2, 4.8, -75.0, List.of(), List.of());

        when(stayService.findAllDTOs()).thenReturn(List.of(s1, s2));

        mockMvc.perform(get("/stays"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].name").value("Cabaña"))
                .andExpect(jsonPath("$[1].location").value("Campo"));
    }

    @Test
    @DisplayName("Debería retornar un stay por ID")
    void shouldReturnStayById() throws Exception {
        StayDTO stay = new StayDTO(3L, "EcoLodge", "Eco", List.of("img3.jpg"), "Selva", 3, 150.0, 1, 1, 1, 5.0, -76.0, List.of(), List.of());

        when(stayService.findDTOById(3L)).thenReturn(stay);

        mockMvc.perform(get("/stays/3"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("EcoLodge"))
                .andExpect(jsonPath("$.location").value("Selva"));
    }

    @Test
    @DisplayName("Debería actualizar un stay")
    void shouldUpdateStay() throws Exception {
        StayRequest request = new StayRequest();
        StayDTO updated = new StayDTO(4L, "Modificado", "Desc", List.of("img.jpg"), "Zona nueva", 2, 110.0, 1, 1, 1, 4.3, -73.9, List.of(), List.of());

        when(stayService.updateStay(eq(4L), any())).thenReturn(updated);

        mockMvc.perform(put("/stays/4")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Modificado"))
                .andExpect(jsonPath("$.location").value("Zona nueva"));
    }

    @Test
    @DisplayName("Debería eliminar un stay")
    void shouldDeleteStay() throws Exception {
        doNothing().when(stayService).delete(5L);

        mockMvc.perform(delete("/stays/5"))
                .andExpect(status().isNoContent());

        verify(stayService, times(1)).delete(5L);
    }
}
