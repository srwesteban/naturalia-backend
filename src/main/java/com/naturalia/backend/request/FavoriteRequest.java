package com.naturalia.backend.request;

import lombok.Data;

@Data
public class FavoriteRequest {
    private Long userId;
    private Long stayId;
}