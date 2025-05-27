package com.naturalia.backend.dto;

import com.naturalia.backend.entity.StayType;

public class StaySummaryDTO {
    private Long id;
    private String name;
    private StayType type;

    public StaySummaryDTO(Long id, String name, StayType type) {
        this.id = id;
        this.name = name;
        this.type = type;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public StayType getType() {
        return type;
    }

    public void setType(StayType type) {
        this.type = type;
    }
}
