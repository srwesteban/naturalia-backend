package com.naturalia.backend.service;

import com.naturalia.backend.dto.StayDTO;
import com.naturalia.backend.dto.StayRequest;
import com.naturalia.backend.dto.StaySummaryDTO;
import com.naturalia.backend.entity.Stay;
import com.naturalia.backend.exception.ResourceNotFoundException;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface IStayService {

    Stay save(Stay stay);

    Optional<Stay> findById(Long id) throws ResourceNotFoundException;

    void update(Stay stay) throws Exception;

    void delete(Long id) throws ResourceNotFoundException;

    StayDTO create(StayRequest request);

    StayDTO updateStay(Long id, StayRequest request);

    List<StayDTO> findAll();

    List<StayDTO> findAllDTOs();

    StayDTO findDTOById(Long id);

    List<Stay> findAvailableStays(LocalDate checkIn, LocalDate checkOut);

    List<StayDTO> getSuggestionsByName(String query);

    List<StayDTO> findRecommended();

    List<StaySummaryDTO> findAllSummaries();




}
