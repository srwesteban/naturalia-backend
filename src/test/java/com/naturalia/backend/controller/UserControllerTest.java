package com.naturalia.backend.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.naturalia.backend.entity.Role;
import com.naturalia.backend.entity.User;
import com.naturalia.backend.repository.IUserRepository;
import com.naturalia.backend.service.IUserService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
public class UserControllerTest {

    @MockBean
    private IUserRepository userRepository;


    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private IUserService userService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @DisplayName("Debería retornar lista de usuarios")
    public void shouldReturnListOfUsers() throws Exception {
        User user1 = User.builder().id(1L).firstname("Juan").lastname("Pérez").email("juan@example.com").role(Role.USER).build();
        User user2 = User.builder().id(2L).firstname("Ana").lastname("López").email("ana@example.com").role(Role.HOST).build();

        when(userService.findAll()).thenReturn(List.of(user1, user2));

        mockMvc.perform(get("/users"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].firstname").value("Juan"))
                .andExpect(jsonPath("$[1].email").value("ana@example.com"));
    }

    @Test
    @DisplayName("Debería retornar lista de hosts desde el repositorio")
    public void shouldReturnListOfHostsFromRepository() throws Exception {
        User user1 = User.builder().id(10L).firstname("Carlos").lastname("Ramírez").role(Role.HOST).build();
        User user2 = User.builder().id(11L).firstname("Luisa").lastname("Martínez").role(Role.HOST).build();

        when(userRepository.findByRole(Role.HOST)).thenReturn(List.of(user1, user2));

        mockMvc.perform(get("/users/hosts"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].firstname").value("Carlos"))
                .andExpect(jsonPath("$[1].lastname").value("Martínez"));
    }


    @Test
    @DisplayName("Debería actualizar el rol del usuario")
    public void shouldUpdateUserRole() throws Exception {
        Long userId = 1L;
        Role newRole = Role.ADMIN;

        User updatedUser = User.builder()
                .id(userId)
                .firstname("David")
                .lastname("Esteban")
                .email("david@example.com")
                .role(newRole)
                .build();

        when(userService.changeRole(userId, newRole)).thenReturn(updatedUser);

        mockMvc.perform(put("/users/{userId}/role", userId)
                        .param("role", "ADMIN"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.role").value("ADMIN"))
                .andExpect(jsonPath("$.firstname").value("David"));
    }


}
