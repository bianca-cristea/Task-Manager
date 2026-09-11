package com.bianca.backend.controllers;

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
            @RequestParam(defaultValue = "0") Integer pageNumber,
            @RequestParam(defaultValue = "10") Integer pageSize,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "asc") String orderDir
    ) {

        CategoryResponse categoryResponse = categoryService.getAllCategories(
                pageNumber,
                pageSize,
                sortBy,
                orderDir
        );

        return ResponseEntity.ok(categoryResponse);
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
