package com.naturalia.backend.service;

import com.naturalia.backend.dto.UserDTO;
import com.naturalia.backend.entity.Role;
import com.naturalia.backend.entity.User;
import com.naturalia.backend.repository.IUserRepository;
import com.naturalia.backend.service.impl.UserServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class UserServiceImplTest {

    @Mock
    private IUserRepository userRepository;

    @InjectMocks
    private UserServiceImpl userService;

    private User user1;
    private User user2;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        user1 = User.builder()
                .id(1L)
                .firstname("Alice")
                .lastname("Smith")
                .email("alice@example.com")
                .role(Role.USER)
                .build();

        user2 = User.builder()
                .id(2L)
                .firstname("Bob")
                .lastname("Jones")
                .email("bob@example.com")
                .role(Role.ADMIN)
                .build();
    }

    @Test
    void findAll_returnsUsers() {
        when(userRepository.findAll()).thenReturn(List.of(user1, user2));

        List<User> users = userService.findAll();

        assertEquals(2, users.size());
        assertTrue(users.contains(user1));
        assertTrue(users.contains(user2));
        verify(userRepository).findAll();
    }

    @Test
    void changeRole_existingUser_changesRole() {
        when(userRepository.findById(1L)).thenReturn(Optional.of(user1));
        when(userRepository.save(any(User.class))).thenAnswer(invocation -> invocation.getArgument(0));

        User updatedUser = userService.changeRole(1L, Role.ADMIN);

        assertEquals(Role.ADMIN, updatedUser.getRole());
        verify(userRepository).findById(1L);
        verify(userRepository).save(user1);
    }

    @Test
    void changeRole_userNotFound_throws() {
        when(userRepository.findById(99L)).thenReturn(Optional.empty());

        RuntimeException ex = assertThrows(RuntimeException.class, () -> {
            userService.changeRole(99L, Role.ADMIN);
        });

        assertEquals("User not found", ex.getMessage());
        verify(userRepository).findById(99L);
        verify(userRepository, never()).save(any());
    }

    @Test
    void findAllByRole_returnsUserDTOs() {
        when(userRepository.findByRole(Role.USER)).thenReturn(List.of(user1));

        List<UserDTO> dtos = userService.findAllByRole(Role.USER);

        assertEquals(1, dtos.size());
        UserDTO dto = dtos.get(0);
        assertEquals(user1.getId(), dto.getId());
        assertEquals(user1.getFirstname(), dto.getFirstname());
        assertEquals(user1.getLastname(), dto.getLastname());
        assertEquals(user1.getEmail(), dto.getEmail());

        verify(userRepository).findByRole(Role.USER);
    }
}
