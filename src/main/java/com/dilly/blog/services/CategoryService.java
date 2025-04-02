package com.dilly.blog.services;

import com.dilly.blog.domain.entities.Category;

import java.util.List;

public interface CategoryService {
    /**
     * Lists all categories with their post counts.
     */
    List<Category> listCategories();
}
