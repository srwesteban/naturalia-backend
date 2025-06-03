package com.naturalia.backend.mapper;

import com.naturalia.backend.dto.FeatureDTO;
import com.naturalia.backend.entity.Feature;
import org.springframework.stereotype.Component;

@Component
public class FeatureMapper {

    public FeatureDTO toDTO(Feature feature) {
        return FeatureDTO.builder()
                .id(feature.getId())
                .name(feature.getName())
                .icon(feature.getIcon())
                .build();
    }
}
