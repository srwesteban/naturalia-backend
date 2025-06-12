package com.naturalia.backend.service;

import com.naturalia.backend.entity.Category;
import com.naturalia.backend.repository.ICategoryRepository;
import com.naturalia.backend.repository.IStayRepository;
import com.naturalia.backend.service.impl.CategoryServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class CategoryServiceImplTest {

    @Mock
    private ICategoryRepository categoryRepository;

    @Mock
    private IStayRepository stayRepository;

    @InjectMocks
    private CategoryServiceImpl categoryService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void createCategory_success() {
        Category cat = new Category();
        cat.setId(1L);
        cat.setTitle("Camping");

        when(categoryRepository.save(cat)).thenReturn(cat);

        Category saved = categoryService.createCategory(cat);

        assertEquals(cat, saved);
        verify(categoryRepository).save(cat);
    }

    @Test
    void getAllCategories_success() {
        Category c1 = new Category();
        c1.setId(1L);
        c1.setTitle("A");

        Category c2 = new Category();
        c2.setId(2L);
        c2.setTitle("B");

        when(categoryRepository.findAll()).thenReturn(Arrays.asList(c1, c2));

        List<Category> categories = categoryService.getAllCategories();

        assertEquals(2, categories.size());
        verify(categoryRepository).findAll();
    }

    @Test
    void deleteCategory_success() {
        Category cat = new Category();
        cat.setId(1L);

        when(categoryRepository.findById(1L)).thenReturn(Optional.of(cat));
        when(stayRepository.existsByCategory(cat)).thenReturn(false);

        categoryService.deleteCategory(1L);

        verify(categoryRepository).deleteById(1L);
    }

    @Test
    void deleteCategory_categoryNotFound_throws() {
        when(categoryRepository.findById(1L)).thenReturn(Optional.empty());

        RuntimeException ex = assertThrows(RuntimeException.class, () -> {
            categoryService.deleteCategory(1L);
        });

        assertEquals("Categoría no encontrada", ex.getMessage());
        verify(categoryRepository, never()).deleteById(any());
    }

    @Test
    void deleteCategory_categoryUsed_throws() {
        Category cat = new Category();
        cat.setId(1L);

        when(categoryRepository.findById(1L)).thenReturn(Optional.of(cat));
        when(stayRepository.existsByCategory(cat)).thenReturn(true);

        RuntimeException ex = assertThrows(RuntimeException.class, () -> {
            categoryService.deleteCategory(1L);
        });

        assertEquals("No se puede eliminar: categoría asociada a uno o más productos", ex.getMessage());
        verify(categoryRepository, never()).deleteById(any());
    }
}
