package com.naturalia.backend.service;

import com.naturalia.backend.entity.Category;

import java.util.List;

public interface ICategoryService {
    Category createCategory(Category category);
    List<Category> getAllCategories();
    void deleteCategory(Long id);
}
