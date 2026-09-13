package com.bianca.backend.services.impl;

import com.bianca.backend.dtos.CategoryDTO;
import com.bianca.backend.models.Category;
import com.bianca.backend.repositories.CategoryRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CategoryServiceImplTest {

    @Mock
    private CategoryRepository categoryRepository;

    @Mock
    private ModelMapper modelMapper;

    @InjectMocks
    private CategoryServiceImpl categoryService;

    @Test
    void getCategoryById_shouldReturnCategory_whenCategoryExists() {

        Category category = new Category();
        category.setCategoryId(1L);
        category.setTitle("Urgent");

        CategoryDTO categoryDTO = new CategoryDTO();
        categoryDTO.setCategoryId(1L);
        categoryDTO.setTitle("Urgent");

        when(categoryRepository.findById(1L)).thenReturn(Optional.of(category));
        when(modelMapper.map(category, CategoryDTO.class)).thenReturn(categoryDTO);


        CategoryDTO result = categoryService.getCategoryById(1L);


        assertEquals("Urgent", result.getTitle());
        assertEquals(1L, result.getCategoryId());
    }
}