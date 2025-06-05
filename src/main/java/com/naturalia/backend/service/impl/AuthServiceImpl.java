package com.naturalia.backend.service.impl;

import com.naturalia.backend.authentication.RegisterRequest;
import com.naturalia.backend.entity.Role;
import com.naturalia.backend.entity.User;
import com.naturalia.backend.repository.IUserRepository;
import com.naturalia.backend.service.IAuthService;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import java.util.Optional;

@Service
public class AuthServiceImpl implements IAuthService {

    private final IUserRepository IUserRepository;
    private final PasswordEncoder passwordEncoder;

    public AuthServiceImpl(IUserRepository IUserRepository, PasswordEncoder passwordEncoder) {
        this.IUserRepository = IUserRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public User register(RegisterRequest request) {
        Optional<User> existing = IUserRepository.findByEmail(request.getEmail());
        if (existing.isPresent()) {
            throw new IllegalArgumentException("Email already in use");
        }
        User user = User.builder()
                .firstname(request.getFirstname())
                .lastname(request.getLastname())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .role(Role.USER)
                .build();
        return IUserRepository.save(user);
    }

    @Override
    public User getAuthenticatedUser() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String email = auth.getName();
        return IUserRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));
    }
}