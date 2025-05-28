package com.naturalia.backend.service.impl;

import com.naturalia.backend.dto.*;
import com.naturalia.backend.entity.Feature;
import com.naturalia.backend.entity.Stay;
import com.naturalia.backend.entity.StayType;
import com.naturalia.backend.exception.DuplicateNameException;
import com.naturalia.backend.exception.ResourceNotFoundException;
import com.naturalia.backend.repository.IFeatureRepository;
import com.naturalia.backend.repository.IStayRepository;
import com.naturalia.backend.service.IStayService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class StayServiceImpl implements IStayService {

    private final IStayRepository stayRepository;
    private final IFeatureRepository featureRepository;

    @Override
    public Stay save(Stay stay) {
        if (stayRepository.existsByname(stay.getName())) {
            throw new DuplicateNameException("DUPLICATE_NAME");
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
    public List<StayDTO> findAll() {
        return stayRepository.findAll()
                .stream()
                .map(this::convertToDTO)
                .toList();
    }

    @Override
    public List<StaySummaryDTO> findAllSummaries() {
        return stayRepository.findAll()
                .stream()
                .map(stay -> new StaySummaryDTO(stay.getId(), stay.getName(), stay.getType()))
                .collect(Collectors.toList());
    }

    @Override
    public StayDTO create(StayRequest request) {
        System.out.println("Creando Stay con features: " + request.getFeatureIds());

        List<Feature> features = featureRepository.findAllById(
                request.getFeatureIds() != null ? request.getFeatureIds() : List.of()
        );

        Stay stay = Stay.builder()
                .name(request.getName())
                .description(request.getDescription())
                .images(request.getImages())
                .location(request.getLocation())
                .capacity(request.getCapacity())
                .pricePerNight(request.getPricePerNight())
                .type(request.getType())
                .features(features)
                .build();

        Stay saved = stayRepository.save(stay);

        return convertToDTO(saved);
    }

    @Override
    public StayDTO updateStay(Long id, StayRequest request) {
        Stay stay = stayRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Stay not found"));

        List<Feature> features = featureRepository.findAllById(
                request.getFeatureIds() != null ? request.getFeatureIds() : List.of()
        );

        stay.setName(request.getName());
        stay.setDescription(request.getDescription());
        stay.setImages(request.getImages());
        stay.setLocation(request.getLocation());
        stay.setCapacity(request.getCapacity());
        stay.setPricePerNight(request.getPricePerNight());
        stay.setType(request.getType());
        stay.setFeatures(features);

        return convertToDTO(stayRepository.save(stay));
    }

    @Override
    public List<StayDTO> findAllDTOs() {
        return stayRepository.findAll()
                .stream()
                .map(this::convertToDTO)
                .toList();
    }

    @Override
    public List<StayDTO> findDTOsByTypes(List<StayType> types) {
        return stayRepository.findByTypeIn(types)
                .stream()
                .map(this::convertToDTO)
                .toList();
    }

    @Override
    public StayDTO findDTOById(Long id) {
        Stay stay = stayRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Stay not found with id: " + id));
        return convertToDTO(stay);
    }



    private StayDTO convertToDTO(Stay stay) {
        return StayDTO.builder()
                .id(stay.getId())
                .name(stay.getName())
                .description(stay.getDescription())
                .images(stay.getImages())
                .location(stay.getLocation())
                .capacity(stay.getCapacity())
                .pricePerNight(stay.getPricePerNight())
                .type(stay.getType())
                .features(
                        stay.getFeatures().stream()
                                .map(feature -> FeatureDTO.builder()
                                        .id(feature.getId())
                                        .name(feature.getName())
                                        .icon(feature.getIcon())
                                        .build())
                                .collect(Collectors.toList())
                )
                .build();
    }

    @Override
    public List<Stay> findByTypes(List<StayType> types) {
        return stayRepository.findByTypeIn(types);
    }
}
