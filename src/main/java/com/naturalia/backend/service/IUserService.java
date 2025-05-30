package com.naturalia.backend.service;

import com.naturalia.backend.dto.UserDTO;
import com.naturalia.backend.entity.Role;
import com.naturalia.backend.entity.User;

import java.util.List;

public interface IUserService {
    List<User> findAll();
    User changeRole(Long userId, Role newRole);
    List<UserDTO> findAllByRole(Role role);

}
