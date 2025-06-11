package com.naturalia.backend.dto;

import lombok.*;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class StayListCardDTO {
    private Long id;
    private String name;
    private String description;
    private String imageUrl;
    private String location;
    private double pricePerNight;
    private List<CategoryTitleDTO> categories;

}
