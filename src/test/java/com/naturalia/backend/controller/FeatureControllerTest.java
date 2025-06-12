package com.naturalia.backend.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.naturalia.backend.authentication.AuthenticationRequest;
import com.naturalia.backend.authentication.RegisterRequest;
import com.naturalia.backend.dto.FeatureRequest;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class FeatureControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    private String token;
    private Long createdFeatureId;

    @BeforeAll
    void setupAuth() throws Exception {
        String suffix = String.valueOf(System.currentTimeMillis());
        RegisterRequest register = new RegisterRequest(
                "Feat", "Tester", "feat" + suffix + "@mail.com", "123456",
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
    }

    @Test
    @Order(1)
    @DisplayName("✅ Crear feature")
    void testCreateFeature() throws Exception {
        FeatureRequest request = new FeatureRequest();
        request.setName("Piscina");
        request.setIcon("pool-icon");

        MvcResult result = mockMvc.perform(post("/features")
                        .header("Authorization", "Bearer " + token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Piscina"))
                .andReturn();

        createdFeatureId = objectMapper.readTree(result.getResponse().getContentAsString()).get("id").asLong();
    }

    @Test
    @Order(2)
    @DisplayName("✅ Obtener todas las features")
    void testGetAllFeatures() throws Exception {
        mockMvc.perform(get("/features")
                        .header("Authorization", "Bearer " + token))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray());
    }

    @Test
    @Order(3)
    @DisplayName("✅ Actualizar feature")
    void testUpdateFeature() throws Exception {
        FeatureRequest updateRequest = new FeatureRequest();
        updateRequest.setName("Piscina grande");
        updateRequest.setIcon("big-pool-icon");

        mockMvc.perform(put("/features/{id}", createdFeatureId)
                        .header("Authorization", "Bearer " + token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Piscina grande"))
                .andExpect(jsonPath("$.icon").value("big-pool-icon"));
    }

    @Test
    @Order(4)
    @DisplayName("✅ Eliminar feature")
    void testDeleteFeature() throws Exception {
        mockMvc.perform(delete("/features/{id}", createdFeatureId)
                        .header("Authorization", "Bearer " + token))
                .andExpect(status().isNoContent());
    }
}
