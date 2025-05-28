package com.naturalia.backend.service;

import com.naturalia.backend.dto.FeatureDTO;
import com.naturalia.backend.dto.FeatureRequest;

import java.util.List;

public interface IFeatureService {
    List<FeatureDTO> getAllFeatures();
    FeatureDTO createFeature(FeatureRequest request);
    FeatureDTO updateFeature(Long id, FeatureRequest request);
    void deleteFeature(Long id);
}
