package com.naturalia.backend.service.impl;

import com.naturalia.backend.exception.ResourceNotFoundException;
import com.naturalia.backend.entity.Stay;
import com.naturalia.backend.repository.StayRepository;
import com.naturalia.backend.service.IStayService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class StayService implements IStayService {

    private final StayRepository stayRepository;


    public StayService(StayRepository stayRepository) {
        this.stayRepository = stayRepository;
    }

    @Override
    public Stay save(Stay stay){
        if(stayRepository.existsByname(stay.getName())){
            throw new IllegalArgumentException("A stay with this name already exists");
        }
        return stayRepository.save(stay);
    }


    @Override
    public Optional<Stay> findById(Long id) {
        return stayRepository.findById(id);
    }

    @Override
    public void update(Stay stay) {
        if (!stayRepository.existsById(stay.getId())) {
            throw new ResourceNotFoundException("Stay not found with id: " + stay.getId());
        }
        stayRepository.save(stay);
    }

    @Override
    public void delete(Long id) {
        if (!stayRepository.existsById(id)) {
            throw new ResourceNotFoundException("Stay not found with id: " + id);
        }
        stayRepository.deleteById(id);
    }

    @Override
    public List<Stay> findAll() {
        return stayRepository.findAll();
    }
}
