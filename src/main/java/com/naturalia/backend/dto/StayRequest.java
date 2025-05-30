package com.naturalia.backend.dto;

import com.naturalia.backend.entity.User;
import lombok.*;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class StayRequest {
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

    private List<Long> featureIds;
    private List<Long> categoryIds;

    private Long hostId; // 👈 importante

    public List<Long> getCategoryIds() {
        return categoryIds;
    }

    public void setCategoryIds(List<Long> categoryIds) {
        this.categoryIds = categoryIds;
    }
}
