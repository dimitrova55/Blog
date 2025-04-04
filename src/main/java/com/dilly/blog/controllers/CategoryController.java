package com.dilly.blog.controllers;

import com.dilly.blog.domain.dtos.CreateCategoryRequest;
import com.dilly.blog.domain.entities.Category;
import com.dilly.blog.mappers.CategoryMapper;
import com.dilly.blog.services.CategoryService;
import com.dilly.blog.domain.dtos.CategoryDto;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/categories")
@RequiredArgsConstructor
public class CategoryController {
    private final CategoryService categoryService;
    private final CategoryMapper categoryMapper;

    // GET all categories
    @GetMapping
    public ResponseEntity<List<CategoryDto>> listCategories(){
        List<Category> categories = categoryService.listCategories();
        List<CategoryDto> categoriesDto = categories
                .stream()
                .map(categoryMapper::toDto)
                .toList();

        return ResponseEntity.ok(categoriesDto);
    }

    // Create new category
    @PostMapping
    public ResponseEntity<CategoryDto> createCategory(
            @Valid @RequestBody CreateCategoryRequest createCategoryRequest)
    {
        Category category = categoryMapper.toEntity(createCategoryRequest);
        Category savedCategory = categoryService.createCategory(category);

        System.out.println(categoryMapper.toDto(savedCategory).getId());

        return new ResponseEntity<>(
                categoryMapper.toDto(savedCategory),
                HttpStatus.CREATED
        );
    }

}
