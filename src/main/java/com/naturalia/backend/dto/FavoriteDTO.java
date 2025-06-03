package com.naturalia.backend.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class FavoriteDTO {
    private Long id;
    private Long userId;
    private Long stayId;
    private String stayName;
    private String stayImage;
    private String location;
}
