package com.naturalia.backend.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.naturalia.backend.authentication.AuthenticationRequest;
import com.naturalia.backend.authentication.RegisterRequest;
import com.naturalia.backend.entity.Stay;
import com.naturalia.backend.entity.User;
import com.naturalia.backend.repository.IStayRepository;
import com.naturalia.backend.repository.IUserRepository;
import com.naturalia.backend.request.FavoriteRequest;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class FavoriteControllerTest {

    @Autowired private MockMvc mockMvc;
    @Autowired private ObjectMapper objectMapper;
    @Autowired private IStayRepository stayRepository;
    @Autowired private IUserRepository userRepository;

    private static String token;
    private static Long userId;
    private static Long stayId;

    @BeforeAll
    static void initWait() throws InterruptedException {
        Thread.sleep(500);
    }

    @BeforeEach
    void setup() throws Exception {
        if (token != null && userId != null && stayId != null) return;

        String suffix = String.valueOf(System.currentTimeMillis());
        RegisterRequest register = new RegisterRequest(
                "Test", "User", "fav" + suffix + "@mail.com", "123456",
                "CC", suffix, "+573001010101"
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

        userId = userRepository.findByEmail(register.getEmail()).get().getId();

        List<Stay> stays = stayRepository.findAll();

        if (stays.isEmpty()) {
            // Crear un stay mínimo para pruebas
            User host = userRepository.findById(userId).orElseThrow();
            Stay newStay = new Stay();
            newStay.setName("Test Stay");
            newStay.setDescription("Stay created for tests");
            newStay.setLocation("Colombia");
            newStay.setPricePerNight(100.0);
            newStay.setCapacity(2);
            newStay.setBedrooms(1);
            newStay.setBeds(1);
            newStay.setBathrooms(1);
            newStay.setLatitude(0.0);
            newStay.setLongitude(0.0);
            newStay.setHost(host);

            stayId = stayRepository.save(newStay).getId();
        } else {
            stayId = stays.get(0).getId();
        }
    }

    @Test
    @Order(1)
    @DisplayName("✅ Crear favorito")
    void testAddFavorite() throws Exception {
        FavoriteRequest request = new FavoriteRequest(userId, stayId);

        mockMvc.perform(post("/favorites")
                        .header("Authorization", "Bearer " + token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.userId").value(userId))
                .andExpect(jsonPath("$.stayId").value(stayId))
                .andExpect(jsonPath("$.stayName").isNotEmpty());
    }

    @Test
    @Order(2)
    @DisplayName("✅ Obtener favoritos del usuario")
    void testGetFavorites() throws Exception {
        mockMvc.perform(get("/favorites/user/" + userId)
                        .header("Authorization", "Bearer " + token))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1));
    }

    @Test
    @Order(3)
    @DisplayName("✅ Verificar si es favorito")
    void testCheckFavorite() throws Exception {
        mockMvc.perform(get("/favorites/check")
                        .param("userId", String.valueOf(userId))
                        .param("stayId", String.valueOf(stayId))
                        .header("Authorization", "Bearer " + token))
                .andExpect(status().isOk())
                .andExpect(content().string("true"));
    }

    @Test
    @Order(4)
    @DisplayName("✅ Eliminar favorito")
    void testRemoveFavorite() throws Exception {
        mockMvc.perform(delete("/favorites")
                        .param("userId", String.valueOf(userId))
                        .param("stayId", String.valueOf(stayId))
                        .header("Authorization", "Bearer " + token))
                .andExpect(status().isNoContent());
    }
}
