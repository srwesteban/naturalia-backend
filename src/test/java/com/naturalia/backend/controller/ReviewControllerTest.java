package com.naturalia.backend.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.naturalia.backend.authentication.AuthenticationRequest;
import com.naturalia.backend.authentication.RegisterRequest;
import com.naturalia.backend.dto.ReviewRequest;
import com.naturalia.backend.entity.Stay;
import com.naturalia.backend.entity.User;
import com.naturalia.backend.repository.IStayRepository;
import com.naturalia.backend.repository.IUserRepository;
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
public class ReviewControllerTest {

    @Autowired private MockMvc mockMvc;
    @Autowired private ObjectMapper objectMapper;
    @Autowired private IStayRepository stayRepository;
    @Autowired private IUserRepository userRepository;

    private static String token;
    private static Long userId;
    private static Long stayId;

    @BeforeAll
    static void waitForStartup() throws InterruptedException {
        Thread.sleep(500);
    }

    @BeforeEach
    void setup() throws Exception {
        if (token != null && userId != null && stayId != null) return;

        String suffix = String.valueOf(System.currentTimeMillis());
        RegisterRequest register = new RegisterRequest(
                "Rev", "Tester", "rev" + suffix + "@mail.com", "123456",
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
        userId = userRepository.findByEmail(register.getEmail()).get().getId();

        List<Stay> stays = stayRepository.findAll();

        if (stays.isEmpty()) {
            User host = userRepository.findById(userId).orElseThrow();

            Stay newStay = new Stay();
            newStay.setName("Test Stay");
            newStay.setDescription("Stay para test");
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
    @DisplayName("✅ Obtener reviews de un stay")
    void testGetReviews() throws Exception {
        mockMvc.perform(get("/reviews/stay/" + stayId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray());
    }

    @Test
    @Order(2)
    @DisplayName("✅ Obtener promedio de puntuación de un stay")
    void testGetAverageRating() throws Exception {
        mockMvc.perform(get("/reviews/stay/" + stayId + "/average"))
                .andExpect(status().isOk())
                .andExpect(content().string(org.hamcrest.Matchers.notNullValue()));
    }

    @Test
    @Order(3)
    @DisplayName("✅ Crear nueva review (requiere token)")
    void testCreateReview() throws Exception {
        ReviewRequest request = new ReviewRequest();
        request.setStayId(stayId);
        request.setRating(5);
        request.setComment("Excelente lugar para descansar");

        mockMvc.perform(post("/reviews")
                        .header("Authorization", "Bearer " + token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(content().string("Review saved successfully"));
    }
}
