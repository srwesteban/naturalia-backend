package com.naturalia.backend.service;

import com.naturalia.backend.dto.StayDTO;
import com.naturalia.backend.dto.StayRequest;
import com.naturalia.backend.entity.Stay;
import com.naturalia.backend.exception.ResourceNotFoundException;

import java.util.List;
import java.util.Optional;

public interface IStayService {

    Stay save(Stay stay); // útil para uso interno o testing

    Optional<Stay> findById(Long id) throws ResourceNotFoundException;

    void update(Stay stay) throws Exception;

    void delete(Long id) throws ResourceNotFoundException;

    StayDTO create(StayRequest request);

    StayDTO updateStay(Long id, StayRequest request);

    List<StayDTO> findAll();

    List<StayDTO> findAllDTOs();

    StayDTO findDTOById(Long id);
}
