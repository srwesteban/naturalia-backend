package com.naturalia.backend.repository;

import com.naturalia.backend.entity.Stay;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface IStayRepository extends JpaRepository<Stay, Long> {

    Optional<Stay> findByName(String name);
    boolean existsByname(String name);

}
