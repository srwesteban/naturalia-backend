package com.naturalia.backend.repository;

import com.naturalia.backend.entity.Policy;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IPolicyRepository extends JpaRepository<Policy, Long> {
}
