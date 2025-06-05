package com.naturalia.backend.service.impl;

import com.naturalia.backend.entity.Category;
import com.naturalia.backend.repository.ICategoryRepository;
import com.naturalia.backend.repository.IStayRepository;
import com.naturalia.backend.service.ICategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CategoryServiceImpl implements ICategoryService {

    private final ICategoryRepository categoryRepository;
    private final IStayRepository iStayRepository;

    @Override
    public Category createCategory(Category category) {
        return categoryRepository.save(category);
    }

    @Override
    public List<Category> getAllCategories() {
        return categoryRepository.findAll();
    }



    @Override
    public void deleteCategory(Long id) {
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Categoría no encontrada"));

        boolean isUsed = iStayRepository.existsByCategory(category);
        if (isUsed) {
            throw new RuntimeException("No se puede eliminar: categoría asociada a uno o más productos");
        }

        categoryRepository.deleteById(id);
    }
}
