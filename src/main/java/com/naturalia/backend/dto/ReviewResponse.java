package com.naturalia.backend.dto;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
public class ReviewResponse {
    private String userName;
    private int rating;
    private String comment;
    private LocalDateTime date;
}