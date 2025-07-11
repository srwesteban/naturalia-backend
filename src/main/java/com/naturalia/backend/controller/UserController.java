package com.naturalia.backend.controller;

import com.naturalia.backend.configuration.JwtService;
import com.naturalia.backend.dto.UserSummaryDTO;
import com.naturalia.backend.entity.Role;
import com.naturalia.backend.entity.User;
import com.naturalia.backend.repository.IUserRepository;
import com.naturalia.backend.service.IUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {

    private final IUserService userService;
    private final IUserRepository userRepository;
    private final JwtService jwtUtil;

    @GetMapping
    public ResponseEntity<List<User>> getAllUsers() {
        return ResponseEntity.ok(userService.findAll());
    }

    @PutMapping("/{userId}/role")
    public ResponseEntity<?> updateUserRole(@PathVariable Long userId, @RequestParam Role role) {
        User updated = userService.changeRole(userId, role);

        Map<String, Object> claims = new HashMap<>();
        claims.put("firstname", updated.getFirstname());
        claims.put("email", updated.getEmail());
        claims.put("role", updated.getRole().name());

        String newToken = jwtUtil.generateToken(claims, updated); // ✅

        return ResponseEntity.ok().body(Map.of("token", newToken));
    }


    @GetMapping("/hosts")
    public ResponseEntity<List<UserSummaryDTO>> getHosts() {
        List<User> hosts = userRepository.findByRole(Role.HOST);
        List<UserSummaryDTO> list = hosts.stream()
                .map(u -> new UserSummaryDTO(u.getId(), u.getFirstname(), u.getLastname()))
                .toList();
        return ResponseEntity.ok(list);
    }


}

