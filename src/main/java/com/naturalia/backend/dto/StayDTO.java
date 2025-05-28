package com.naturalia.backend.dto;

import com.naturalia.backend.entity.StayType;
import lombok.*;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class StayDTO {
    private Long id;
    private String name;
    private String description;
    private List<String> images;
    private String location;
    private int capacity;
    private double pricePerNight;
    private StayType type;
    private List<FeatureDTO> features;


}
