package com.naturalia.backend.mapper;

import com.naturalia.backend.dto.StayDTO;
import com.naturalia.backend.entity.Stay;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class StayMapper {

    private final FeatureMapper featureMapper;
    private final CategoryMapper categoryMapper;
    private final UserMapper userMapper;

    public StayDTO toDTO(Stay stay) {
        StayDTO dto = new StayDTO();
        dto.setId(stay.getId());
        dto.setName(stay.getName());
        dto.setDescription(stay.getDescription());
        dto.setImages(stay.getImages());
        dto.setLocation(stay.getLocation());
        dto.setCapacity(stay.getCapacity());
        dto.setPricePerNight(stay.getPricePerNight());
        dto.setBedrooms(stay.getBedrooms());
        dto.setBeds(stay.getBeds());
        dto.setBathrooms(stay.getBathrooms());
        dto.setLatitude(stay.getLatitude());
        dto.setLongitude(stay.getLongitude());

        if (stay.getFeatures() != null) {
            dto.setFeatures(stay.getFeatures().stream()
                    .map(featureMapper::toDTO)
                    .toList());
        }

        if (stay.getCategories() != null) {
            dto.setCategories(stay.getCategories().stream()
                    .map(categoryMapper::toDTO)
                    .toList());
        }

        // ✅ Host como DTO
        if (stay.getHost() != null) {
            dto.setHost(userMapper.toDTO(stay.getHost()));
        }

        return dto;
    }
}

