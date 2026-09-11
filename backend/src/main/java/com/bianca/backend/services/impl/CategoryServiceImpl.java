package com.bianca.backend.services.impl;

import com.bianca.backend.dtos.CategoryDTO;
import com.bianca.backend.dtos.CategoryResponse;
import com.bianca.backend.exception.APIException;
import com.bianca.backend.exception.ResourceNotFoundException;
import com.bianca.backend.models.Category;
import com.bianca.backend.repositories.CategoryRepository;
import com.bianca.backend.services.CategoryService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CategoryServiceImpl implements CategoryService {

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private ModelMapper modelMapper;

    @Override
    public CategoryResponse getAllCategories(Integer pageNumber, Integer pageSize, String sortBy, String orderDir) {
        Sort sortByAndOrder = orderDir.equalsIgnoreCase("asc")?
                Sort.by(sortBy).ascending():
                Sort.by(sortBy).descending();

        Pageable pageRequest = PageRequest.of(pageNumber,pageSize,sortByAndOrder);
        Page<Category> categoryPage = categoryRepository.findAll(pageRequest);

        List<Category> categoryList = categoryPage.getContent();

        if(categoryList.isEmpty()){throw new APIException("NO category found");}

        List<CategoryDTO> content= categoryList.stream().map(category -> modelMapper.map(category,CategoryDTO.class)).toList();

        CategoryResponse categoryResponse = new CategoryResponse();
        categoryResponse.setContent(content);
        categoryResponse.setPageNumber(categoryPage.getNumber());
        categoryResponse.setPageSize(categoryPage.getSize());
        categoryResponse.setTotalElements(categoryPage.getTotalElements());
        categoryResponse.setTotalPages(categoryPage.getTotalPages());
        categoryResponse.setLastPage(categoryPage.isLast());

        return categoryResponse;
    }

    @Override
    public CategoryDTO getCategoryById(Long id) {
        Category category = categoryRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Category","categoryId",id));
        return modelMapper.map(category,CategoryDTO.class);
    }

    @Override
    public CategoryDTO createCategory(CategoryDTO categoryDTO) {

        Category category = new Category();
        category.setTitle(categoryDTO.getTitle());
        categoryRepository.save(category);

        return modelMapper.map(category,CategoryDTO.class);

    }

    @Override
    public CategoryDTO updateCategory(Long id, CategoryDTO categoryDTO) {

        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new APIException("Category not found with id: " + id));

        category.setTitle(categoryDTO.getTitle());

        Category updatedCategory = categoryRepository.save(category);

        return modelMapper.map(updatedCategory, CategoryDTO.class);
    }

    @Override
    public CategoryDTO deleteCategory(Long id) {

        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new APIException("Category not found with id: " + id));

        categoryRepository.delete(category);

        return modelMapper.map(category, CategoryDTO.class);
    }
}
