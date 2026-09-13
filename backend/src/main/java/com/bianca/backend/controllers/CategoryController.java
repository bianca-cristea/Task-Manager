package com.bianca.backend.controllers;

import com.bianca.backend.config.AppConstants;
import com.bianca.backend.dtos.CategoryDTO;
import com.bianca.backend.dtos.CategoryResponse;
import com.bianca.backend.services.CategoryService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/categories")
public class CategoryController {


    private final CategoryService categoryService;

    public CategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @GetMapping
    public ResponseEntity<CategoryResponse> getAllCategories(
      @RequestParam(name = "pageNumber", defaultValue = AppConstants.PAGE_NUMBER, required = false) Integer pageNumber,
      @RequestParam(name = "pageSize", defaultValue = AppConstants.PAGE_SIZE, required = false) Integer pageSize,
      @RequestParam(name = "sortBy", defaultValue = AppConstants.SORT_CATEGORIES_BY, required = false) String sortBy,
      @RequestParam(name = "orderDir", defaultValue = AppConstants.SORT_DIR, required = false) String orderDir
    ) {
        return new ResponseEntity<>(categoryService.getAllCategories(pageNumber,pageSize,sortBy,orderDir),HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<CategoryDTO> getCategoryById(@PathVariable Long id) {

        CategoryDTO categoryDTO = categoryService.getCategoryById(id);

        return ResponseEntity.ok(categoryDTO);
    }

    @PostMapping
    public ResponseEntity<CategoryDTO> createCategory(
            @RequestBody CategoryDTO categoryDTO
    ) {

        CategoryDTO createdCategory = categoryService.createCategory(categoryDTO);

        return new ResponseEntity<>(createdCategory, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<CategoryDTO> updateCategory(
            @PathVariable Long id,
            @RequestBody CategoryDTO categoryDTO
    ) {

        CategoryDTO updatedCategory = categoryService.updateCategory(id, categoryDTO);

        return ResponseEntity.ok(updatedCategory);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<CategoryDTO> deleteCategory(@PathVariable Long id) {

        CategoryDTO deletedCategory = categoryService.deleteCategory(id);

        return ResponseEntity.ok(deletedCategory);
    }


}
