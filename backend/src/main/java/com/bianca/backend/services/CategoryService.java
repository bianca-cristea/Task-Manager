package com.bianca.backend.services;

import com.bianca.backend.dtos.CategoryDTO;
import com.bianca.backend.dtos.CategoryResponse;

public interface CategoryService {
    public CategoryResponse getAllCategories(Integer pageNumber, Integer pageSize, String sortBy, String orderDir);
    public CategoryDTO getCategoryById(Long id);
    public CategoryDTO createCategory(CategoryDTO categoryDTO);
    public CategoryDTO updateCategory(Long id, CategoryDTO categoryDTO);
    public CategoryDTO deleteCategory(Long id);


}
