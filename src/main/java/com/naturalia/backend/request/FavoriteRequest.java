package com.naturalia.backend.request;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class FavoriteRequest {
    private Long userId;
    private Long stayId;
}