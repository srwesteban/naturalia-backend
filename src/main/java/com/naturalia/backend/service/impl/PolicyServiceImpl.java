package com.naturalia.backend.service.impl;

import com.naturalia.backend.entity.Policy;
import com.naturalia.backend.repository.IPolicyRepository;
import com.naturalia.backend.service.IPolicyService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PolicyServiceImpl implements IPolicyService {

    private final IPolicyRepository repository;

    @Override
    public List<Policy> getAll() {
        return repository.findAll();
    }
}
