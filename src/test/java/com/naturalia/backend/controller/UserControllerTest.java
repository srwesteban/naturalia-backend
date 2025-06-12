package com.naturalia.backend.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.naturalia.backend.authentication.AuthenticationRequest;
import com.naturalia.backend.authentication.RegisterRequest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
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
public class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @DisplayName("✅ Cambiar rol de usuario")
    void shouldChangeUserRole() throws Exception {
        RegisterRequest register = new RegisterRequest(
                "Carlos", "Tester", "carlos" + System.currentTimeMillis() + "@test.com", "clave123",
                "CC", "99999999", "+573002222222"
        );

        // Registro
        mockMvc.perform(post("/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(register)))
                .andExpect(status().isOk());

        // Login
        AuthenticationRequest login = new AuthenticationRequest(register.getEmail(), register.getPassword());

        MvcResult loginResult = mockMvc.perform(post("/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(login)))
                .andExpect(status().isOk())
                .andReturn();

        String token = objectMapper.readTree(loginResult.getResponse().getContentAsString()).get("token").asText();

        // Obtener ID
        MvcResult meResult = mockMvc.perform(get("/auth/me")
                        .header("Authorization", "Bearer " + token))
                .andExpect(status().isOk())
                .andReturn();

        Long userId = objectMapper.readTree(meResult.getResponse().getContentAsString()).get("id").asLong();

        // Cambiar rol a HOST
        mockMvc.perform(put("/users/" + userId + "/role")
                        .param("role", "HOST")
                        .header("Authorization", "Bearer " + token))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("✅ Eliminar usuario")
    void shouldDeleteUser() throws Exception {
        RegisterRequest register = new RegisterRequest(
                "Laura", "Tester", "laura" + System.currentTimeMillis() + "@test.com", "clave123",
                "CC", "77777777", "+573003333333"
        );

        // Registro
        mockMvc.perform(post("/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(register)))
                .andExpect(status().isOk());

        // Login
        AuthenticationRequest login = new AuthenticationRequest(register.getEmail(), register.getPassword());

        MvcResult loginResult = mockMvc.perform(post("/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(login)))
                .andExpect(status().isOk())
                .andReturn();

        String token = objectMapper.readTree(loginResult.getResponse().getContentAsString()).get("token").asText();

        // Obtener ID
        MvcResult meResult = mockMvc.perform(get("/auth/me")
                        .header("Authorization", "Bearer " + token))
                .andExpect(status().isOk())
                .andReturn();

        Long userId = objectMapper.readTree(meResult.getResponse().getContentAsString()).get("id").asLong();


    }
}
