package com.naturalia.backend.service.impl;

import com.naturalia.backend.authentication.RegisterRequest;
import com.naturalia.backend.entity.Role;
import com.naturalia.backend.entity.User;
import com.naturalia.backend.repository.IUserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class AuthServiceImplTest {

    @Mock
    private IUserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private AuthServiceImpl authService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void register_success() {
        RegisterRequest req = new RegisterRequest();
        req.setFirstname("John");
        req.setLastname("Doe");
        req.setEmail("john@example.com");
        req.setPassword("password123");

        when(userRepository.findByEmail(req.getEmail())).thenReturn(Optional.empty());
        when(passwordEncoder.encode(req.getPassword())).thenReturn("encodedPwd");
        when(userRepository.save(any(User.class))).thenAnswer(i -> i.getArguments()[0]);

        User savedUser = authService.register(req);

        assertEquals("John", savedUser.getFirstname());
        assertEquals("Doe", savedUser.getLastname());
        assertEquals("john@example.com", savedUser.getEmail());
        assertEquals("encodedPwd", savedUser.getPassword());
        assertEquals(Role.USER, savedUser.getRole());

        verify(userRepository).findByEmail(req.getEmail());
        verify(passwordEncoder).encode(req.getPassword());
        verify(userRepository).save(any(User.class));
    }

    @Test
    void register_emailAlreadyExists_throws() {
        RegisterRequest req = new RegisterRequest();
        req.setEmail("existing@example.com");

        when(userRepository.findByEmail(req.getEmail())).thenReturn(Optional.of(new User()));

        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, () -> {
            authService.register(req);
        });

        assertEquals("Email already in use", ex.getMessage());

        verify(userRepository).findByEmail(req.getEmail());
        verify(userRepository, never()).save(any());
        verify(passwordEncoder, never()).encode(any());
    }

    @Test
    void getAuthenticatedUser_success() {
        String email = "authuser@example.com";
        User user = new User();
        user.setEmail(email);

        Authentication authentication = mock(Authentication.class);
        when(authentication.getName()).thenReturn(email);

        SecurityContext securityContext = mock(SecurityContext.class);
        when(securityContext.getAuthentication()).thenReturn(authentication);

        SecurityContextHolder.setContext(securityContext);

        when(userRepository.findByEmail(email)).thenReturn(Optional.of(user));

        User result = authService.getAuthenticatedUser();

        assertEquals(email, result.getEmail());

        verify(userRepository).findByEmail(email);
    }

    @Test
    void getAuthenticatedUser_userNotFound_throws() {
        String email = "notfound@example.com";

        Authentication authentication = mock(Authentication.class);
        when(authentication.getName()).thenReturn(email);

        SecurityContext securityContext = mock(SecurityContext.class);
        when(securityContext.getAuthentication()).thenReturn(authentication);

        SecurityContextHolder.setContext(securityContext);

        when(userRepository.findByEmail(email)).thenReturn(Optional.empty());

        RuntimeException ex = assertThrows(RuntimeException.class, () -> {
            authService.getAuthenticatedUser();
        });

        assertEquals("User not found", ex.getMessage());
        verify(userRepository).findByEmail(email);
    }
}
