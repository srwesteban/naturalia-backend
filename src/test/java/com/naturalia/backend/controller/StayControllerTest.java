package com.naturalia.backend.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.naturalia.backend.authentication.AuthenticationRequest;
import com.naturalia.backend.authentication.RegisterRequest;
import com.naturalia.backend.dto.StayDTO;
import com.naturalia.backend.dto.StayRequest;
import com.naturalia.backend.dto.StayUpdateDTO;
import com.naturalia.backend.entity.Stay;
import com.naturalia.backend.repository.IStayRepository;
import com.naturalia.backend.repository.IUserRepository;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.time.LocalDate;
import java.util.List;

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class StayControllerTest {

    @Autowired private MockMvc mockMvc;
    @Autowired private ObjectMapper objectMapper;
    @Autowired private IStayRepository stayRepository;
    @Autowired private IUserRepository userRepository;

    private String token;
    private Long stayId;
    private Long hostId;

    @BeforeAll
    void setupAuthAndData() throws Exception {
        String suffix = String.valueOf(System.currentTimeMillis());
        RegisterRequest register = new RegisterRequest(
                "Host", "Tester", "host" + suffix + "@mail.com", "123456",
                "CC", suffix, "+573001234567"
        );

        mockMvc.perform(post("/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(register)))
                .andExpect(status().isOk());

        AuthenticationRequest login = new AuthenticationRequest(register.getEmail(), register.getPassword());
        MvcResult result = mockMvc.perform(post("/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(login)))
                .andExpect(status().isOk())
                .andReturn();

        token = objectMapper.readTree(result.getResponse().getContentAsString()).get("token").asText();
        hostId = userRepository.findByEmail(register.getEmail()).get().getId();

        // Crear un stay para pruebas si no hay alguno
        List<Stay> stays = stayRepository.findAll();
        if (stays.isEmpty()) {
            Stay stay = new Stay();
            stay.setName("Test Stay");
            stay.setDescription("Stay para pruebas");
            stay.setLocation("Colombia");
            stay.setPricePerNight(150.0);
            stay.setCapacity(3);
            stay.setBedrooms(2);
            stay.setBeds(2);
            stay.setBathrooms(1);
            stay.setLatitude(1.0);
            stay.setLongitude(1.0);
            stay.setHost(userRepository.findById(hostId).orElseThrow());
            stayId = stayRepository.save(stay).getId();
        } else {
            stayId = stays.get(0).getId();
        }
    }

    @Test
    @Order(1)
    @DisplayName("✅ Crear Stay")
    void testCreateStay() throws Exception {
        StayRequest request = new StayRequest();
        request.setName("Nuevo Stay");
        request.setDescription("Descripción Stay");
        request.setLocation("Bogotá");
        request.setPricePerNight(120.0);
        request.setCapacity(4);
        request.setBedrooms(2);
        request.setBeds(2);
        request.setBathrooms(1);
        request.setLatitude(4.6);
        request.setLongitude(-74.1);
        request.setHostId(hostId);

        mockMvc.perform(post("/stays")
                        .header("Authorization", "Bearer " + token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name").value("Nuevo Stay"))
                .andExpect(jsonPath("$.host.id").value(hostId));
    }

    @Test
    @Order(2)
    @DisplayName("✅ Obtener todos los stays")
    void testGetAllStays() throws Exception {
        mockMvc.perform(get("/stays"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray());
    }

    @Test
    @Order(3)
    @DisplayName("✅ Obtener stay por id")
    void testGetStayById() throws Exception {
        mockMvc.perform(get("/stays/{id}", stayId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(stayId));
    }

    @Test
    @Order(4)
    @DisplayName("✅ Actualizar stay con StayRequest")
    void testUpdateStay() throws Exception {
        StayRequest request = new StayRequest();
        request.setName("Stay Actualizado");
        request.setDescription("Desc actualizada");
        request.setLocation("Medellín");
        request.setPricePerNight(130.0);
        request.setCapacity(5);
        request.setBedrooms(3);
        request.setBeds(3);
        request.setBathrooms(2);
        request.setLatitude(6.2);
        request.setLongitude(-75.6);
        request.setHostId(hostId);

        mockMvc.perform(put("/stays/{id}", stayId)
                        .header("Authorization", "Bearer " + token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Stay Actualizado"));
    }

    @Test
    @Order(5)
    @DisplayName("✅ Actualizar stay con StayUpdateDTO")
    void testUpdateStayFull() throws Exception {
        StayUpdateDTO dto = new StayUpdateDTO();
        dto.setName("Stay Full Update");
        dto.setDescription("Update completo");
        dto.setLocation("Cartagena");
        dto.setPricePerNight(140.0);
        dto.setCapacity(6);
        dto.setBedrooms(3);
        dto.setBeds(4);
        dto.setBathrooms(2);
        dto.setLatitude(10.4);
        dto.setLongitude(-75.5);
        dto.setHostId(hostId);
        // agrega más campos que tenga StayUpdateDTO si necesitas

        mockMvc.perform(put("/stays/{id}/full", stayId)
                        .header("Authorization", "Bearer " + token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isOk());
    }

    @Test
    @Order(6)
    @DisplayName("✅ Buscar stays disponibles (search)")
    void testSearchAvailableStays() throws Exception {
        LocalDate checkIn = LocalDate.now().plusDays(1);
        LocalDate checkOut = LocalDate.now().plusDays(3);

        mockMvc.perform(get("/stays/search")
                        .param("checkIn", checkIn.toString())
                        .param("checkOut", checkOut.toString()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray());
    }

    @Test
    @Order(7)
    @DisplayName("✅ Buscar stays disponibles (search-light)")
    void testSearchAvailableLight() throws Exception {
        LocalDate checkIn = LocalDate.now().plusDays(1);
        LocalDate checkOut = LocalDate.now().plusDays(3);

        mockMvc.perform(get("/stays/search-light")
                        .param("checkIn", checkIn.toString())
                        .param("checkOut", checkOut.toString()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray());
    }

    @Test
    @Order(8)
    @DisplayName("✅ Obtener sugerencias")
    void testGetSuggestions() throws Exception {
        mockMvc.perform(get("/stays/suggestions")
                        .param("query", "Test"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray());
    }

    @Test
    @Order(9)
    @DisplayName("✅ Obtener stays recomendados")
    void testGetRecommended() throws Exception {
        mockMvc.perform(get("/stays/recommended"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray());
    }

    @Test
    @Order(10)
    @DisplayName("✅ Obtener resumen stays")
    void testGetStaySummaries() throws Exception {
        mockMvc.perform(get("/stays/summary"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray());
    }

    @Test
    @Order(11)
    @DisplayName("✅ Obtener list cards")
    void testGetStayListCards() throws Exception {
        mockMvc.perform(get("/stays/list-cards"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray());
    }

    @Test
    @Order(12)
    @DisplayName("✅ Obtener stays por host")
    void testGetStaysByHost() throws Exception {
        mockMvc.perform(get("/stays/host/" + hostId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray());
    }

    @Test
    @Order(13)
    @DisplayName("✅ Eliminar stay")
    void testDeleteStay() throws Exception {
        // Primero creamos uno para borrar
        StayRequest request = new StayRequest();
        request.setName("Stay to Delete");
        request.setDescription("Para borrar");
        request.setLocation("Test");
        request.setPricePerNight(100.0);
        request.setCapacity(2);
        request.setBedrooms(1);
        request.setBeds(1);
        request.setBathrooms(1);
        request.setLatitude(0.0);
        request.setLongitude(0.0);
        request.setHostId(hostId);

        MvcResult result = mockMvc.perform(post("/stays")
                        .header("Authorization", "Bearer " + token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andReturn();

        Long idToDelete = objectMapper.readTree(result.getResponse().getContentAsString()).get("id").asLong();

        // Ahora borramos
        mockMvc.perform(delete("/stays/{id}", idToDelete)
                        .header("Authorization", "Bearer " + token))
                .andExpect(status().isNoContent());
    }
}
