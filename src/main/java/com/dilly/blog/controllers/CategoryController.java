package com.dilly.blog.controllers;

import com.dilly.blog.domain.entities.Category;
import com.dilly.blog.mappers.CategoryMapper;
import com.dilly.blog.services.CategoryService;
import com.dilly.blog.domain.dtos.CategoryDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/categories")
@RequiredArgsConstructor
public class CategoryController {
    private final CategoryService categoryService;
    private final CategoryMapper categoryMapper;

    @GetMapping
    public ResponseEntity<List<CategoryDto>> listCategories(){
        List<Category> categories = categoryService.listCategories();
        List<CategoryDto> categoriesDto = categories
                .stream()
                .map(categoryMapper::toDto)
                .toList();

        return ResponseEntity.ok(categoriesDto);
    }

}
