package com.naturalia.backend.service.impl;

import com.naturalia.backend.dto.FeatureDTO;
import com.naturalia.backend.dto.FeatureRequest;
import com.naturalia.backend.entity.Feature;
import com.naturalia.backend.repository.IFeatureRepository;
import com.naturalia.backend.service.IFeatureService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class FeatureServiceImpl implements IFeatureService {

    private final IFeatureRepository featureRepository;

    @Override
    public List<FeatureDTO> getAllFeatures() {
        return featureRepository.findAll()
                .stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Override
    public FeatureDTO createFeature(FeatureRequest request) {
        Feature feature = Feature.builder()
                .name(request.getName())
                .icon(request.getIcon())
                .build();
        return convertToDTO(featureRepository.save(feature));
    }

    @Override
    public FeatureDTO updateFeature(Long id, FeatureRequest request) {
        Feature feature = featureRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Feature not found with id: " + id));
        feature.setName(request.getName());
        feature.setIcon(request.getIcon());
        return convertToDTO(featureRepository.save(feature));
    }

    @Override
    public void deleteFeature(Long id) {
        featureRepository.deleteById(id);
    }

    private FeatureDTO convertToDTO(Feature feature) {
        return FeatureDTO.builder()
                .id(feature.getId())
                .name(feature.getName())
                .icon(feature.getIcon())
                .build();
    }
}
