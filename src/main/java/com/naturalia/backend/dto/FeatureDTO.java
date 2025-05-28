package com.naturalia.backend.dto;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class FeatureDTO {
    private Long id;
    private String name;
    private String icon;
}
