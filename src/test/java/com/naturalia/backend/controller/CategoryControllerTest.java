package com.naturalia.backend.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.naturalia.backend.entity.Category;
import com.naturalia.backend.service.ICategoryService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
public class CategoryControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ICategoryService categoryService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @DisplayName("Debería retornar todas las categorías")
    void shouldReturnAllCategories() throws Exception {
        Category c1 = new Category(1L, "Glamping", "Tiendas de lujo", "img1.jpg");
        Category c2 = new Category(2L, "Casa Campestre", "Rústico y natural", "img2.jpg");

        when(categoryService.getAllCategories()).thenReturn(List.of(c1, c2));

        mockMvc.perform(get("/categories"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].title").value("Glamping"))
                .andExpect(jsonPath("$[1].imageUrl").value("img2.jpg"));
    }

    @Test
    @DisplayName("Debería crear una categoría")
    void shouldCreateCategory() throws Exception {
        Category category = new Category(null, "Jacuzzi", "Lugares con jacuzzi", "img3.jpg");
        Category created = new Category(3L, category.getTitle(), category.getDescription(), category.getImageUrl());

        when(categoryService.createCategory(any(Category.class))).thenReturn(created);

        mockMvc.perform(post("/categories")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(category)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(3))
                .andExpect(jsonPath("$.title").value("Jacuzzi"));
    }

    @Test
    @DisplayName("Debería eliminar una categoría")
    void shouldDeleteCategory() throws Exception {
        doNothing().when(categoryService).deleteCategory(5L);

        mockMvc.perform(delete("/categories/5"))
                .andExpect(status().isNoContent());

        verify(categoryService).deleteCategory(5L);
    }
}
