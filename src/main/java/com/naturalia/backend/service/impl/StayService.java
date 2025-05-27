package com.naturalia.backend.service.impl;

import com.naturalia.backend.dto.StaySummaryDTO;
import com.naturalia.backend.exception.DuplicateNameException;
import com.naturalia.backend.exception.ResourceNotFoundException;
import com.naturalia.backend.entity.Stay;
import com.naturalia.backend.repository.IStayRepository;
import com.naturalia.backend.service.IStayService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class StayService implements IStayService {

    private final IStayRepository IStayRepository;


    public StayService(IStayRepository IStayRepository) {
        this.IStayRepository = IStayRepository;
    }

    @Override
    public Stay save(Stay stay){
        if(IStayRepository.existsByname(stay.getName())){
            throw new DuplicateNameException("DUPLICATE_NAME");
        }
        return IStayRepository.save(stay);
    }


    @Override
    public Optional<Stay> findById(Long id) {
        return IStayRepository.findById(id);
    }

    @Override
    public void update(Stay stay) {
        if (!IStayRepository.existsById(stay.getId())) {
            throw new ResourceNotFoundException("Stay not found with id: " + stay.getId());
        }
        IStayRepository.save(stay);
    }

    @Override
    public void delete(Long id) {
        if (!IStayRepository.existsById(id)) {
            throw new ResourceNotFoundException("Stay not found with id: " + id);
        }
        IStayRepository.deleteById(id);
    }

    @Override
    public List<Stay> findAll() {
        return IStayRepository.findAll();
    }

    @Override
    public List<StaySummaryDTO> findAllSummaries() {
        return IStayRepository.findAll()
                .stream()
                .map(stay -> new StaySummaryDTO(stay.getId(), stay.getName()))
                .collect(Collectors.toList());
    }


}
