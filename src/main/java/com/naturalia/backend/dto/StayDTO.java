package com.naturalia.backend.dto;

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

    private int bedrooms;
    private int beds;
    private int bathrooms;
    private double latitude;
    private double longitude;

    private List<FeatureDTO> features;
    private List<CategoryDTO> categories;

    private UserDTO host;
}
