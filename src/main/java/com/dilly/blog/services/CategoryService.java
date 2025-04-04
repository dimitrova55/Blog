package com.dilly.blog.services;

import com.dilly.blog.domain.entities.Category;

import java.util.List;
import java.util.UUID;

public interface CategoryService {
    /**
     * Lists all categories with their post counts.
     */
    List<Category> listCategories();

    Category createCategory(Category category);

    void deleteCategory(UUID id);
}
