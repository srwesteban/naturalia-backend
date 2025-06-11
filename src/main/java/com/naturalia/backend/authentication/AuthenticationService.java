package com.naturalia.backend.authentication;

import com.naturalia.backend.configuration.JwtService;
import com.naturalia.backend.entity.Role;
import com.naturalia.backend.entity.User;
import com.naturalia.backend.repository.IUserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class AuthenticationService {

    private final IUserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    public AuthenticationResponse register(RegisterRequest request) {
        var user = User.builder()
                .firstname(request.getFirstname())
                .lastname(request.getLastname())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .documentType(request.getDocumentType())
                .documentNumber(request.getDocumentNumber())
                .phoneNumber(request.getPhoneNumber())
                .role(Role.USER)
                .build();


        userRepository.save(user);

        String jwt = generateTokenWithClaims(user);
        return AuthenticationResponse.builder().token(jwt).build();
    }

    public AuthenticationResponse login(AuthenticationRequest request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getEmail(),
                        request.getPassword()
                )
        );

        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow();

        String jwt = generateTokenWithClaims(user);
        return AuthenticationResponse.builder().token(jwt).build();
    }

    private String generateTokenWithClaims(User user) {
        Map<String, Object> extraClaims = new HashMap<>();
        extraClaims.put("role", user.getRole().name());
        extraClaims.put("firstname", user.getFirstname());
        extraClaims.put("email", user.getEmail());
        extraClaims.put("authorities", user.getAuthorities().stream()
                .map(Object::toString)
                .toList());

        return jwtService.generateToken(extraClaims, user);
    }
}
