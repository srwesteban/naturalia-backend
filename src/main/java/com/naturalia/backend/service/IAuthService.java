package com.naturalia.backend.service;

import com.naturalia.backend.authentication.RegisterRequest;
import com.naturalia.backend.entity.User;

public interface IAuthService {
    User register(RegisterRequest User);
}
