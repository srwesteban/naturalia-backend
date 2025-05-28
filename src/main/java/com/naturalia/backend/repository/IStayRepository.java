package com.naturalia.backend.repository;

import com.naturalia.backend.entity.Stay;
import com.naturalia.backend.entity.StayType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface IStayRepository extends JpaRepository<Stay, Long> {

    Optional<Stay> findByName(String name);
    boolean existsByname(String name);
    List<Stay> findByTypeIn(List<StayType> types);


}
