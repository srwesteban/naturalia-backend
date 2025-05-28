package com.naturalia.backend.dto;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class FeatureRequest {
    private String name;
    private String icon;
}
