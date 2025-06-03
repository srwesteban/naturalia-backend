package com.naturalia.backend.mapper;

import com.naturalia.backend.dto.CategoryDTO;
import com.naturalia.backend.entity.Category;
import org.springframework.stereotype.Component;

@Component
public class CategoryMapper {

    public CategoryDTO toDTO(Category category) {
        return CategoryDTO.builder()
                .id(category.getId())
                .title(category.getTitle())
                .description(category.getDescription())
                .imageUrl(category.getImageUrl())
                .build();
    }
}
