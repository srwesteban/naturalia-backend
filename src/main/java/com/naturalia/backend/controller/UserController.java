package com.naturalia.backend.controller;

import com.naturalia.backend.dto.UserDTO;
import com.naturalia.backend.dto.UserSummaryDTO;
import com.naturalia.backend.entity.Role;
import com.naturalia.backend.entity.User;
import com.naturalia.backend.repository.IUserRepository;
import com.naturalia.backend.service.IUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {

    private final IUserService userService;
    private final IUserRepository userRepository;

    @GetMapping
    public ResponseEntity<List<User>> getAllUsers() {
        return ResponseEntity.ok(userService.findAll());
    }

    @PutMapping("/{userId}/role")
    public ResponseEntity<User> updateUserRole(@PathVariable Long userId, @RequestParam Role role) {
        User updated = userService.changeRole(userId, role);
        return ResponseEntity.ok(updated);
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

