package org.yearup.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.yearup.data.CategoryDao;
import org.yearup.models.Category;

import java.util.Arrays;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
class CategoriesControllerTest {

    @Mock
    private CategoryDao categoryDao;

    @InjectMocks
    private CategoriesController categoriesController;

    private MockMvc mockMvc;
    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(categoriesController).build();
        objectMapper = new ObjectMapper();
    }

    @Test
    void testGetAllCategories() throws Exception {
        // Arrange
        Category category1 = new Category();
        category1.setCategoryId(1);
        category1.setName("Electronics");
        category1.setDescription("Electronic devices");

        Category category2 = new Category();
        category2.setCategoryId(2);
        category2.setName("Clothing");
        category2.setDescription("Apparel and accessories");

        List<Category> categories = Arrays.asList(category1, category2);
        when(categoryDao.getAllCategories()).thenReturn(categories);

        // Act & Assert
        mockMvc.perform(get("/categories"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].name").value("Electronics"))
                .andExpect(jsonPath("$[1].name").value("Clothing"));

        verify(categoryDao).getAllCategories();
    }

    @Test
    void testGetCategoryById() throws Exception {
        // Arrange
        Category category = new Category();
        category.setCategoryId(1);
        category.setName("Electronics");
        category.setDescription("Electronic devices");

        when(categoryDao.getById(1)).thenReturn(category);

        // Act & Assert
        mockMvc.perform(get("/categories/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.categoryId").value(1))
                .andExpect(jsonPath("$.name").value("Electronics"));

        verify(categoryDao).getById(1);
    }

    @Test
    void testGetCategoryByIdNotFound() throws Exception {
        // Arrange
        when(categoryDao.getById(999)).thenReturn(null);

        // Act & Assert
        mockMvc.perform(get("/categories/999"))
                .andExpect(status().isNotFound());

        verify(categoryDao).getById(999);
    }
}