package com.dilly.blog.services.impl;

import com.dilly.blog.domain.entities.Category;
import com.dilly.blog.repositories.CategoryRepository;
import com.dilly.blog.services.CategoryService;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CategoryServiceImpl implements CategoryService {

    private final CategoryRepository categoryRepository;

    @Override
    public List<Category> listCategories() {
        return categoryRepository.findAllWithPostCount();
    }

    @Override
    @Transactional
    public Category createCategory(Category category) {

        // Check if category with the same name already exists.
        if(categoryRepository.existsByNameIgnoreCase(category.getName())) {
            throw new IllegalArgumentException("Category already exists with name: " + category.getName());
        }
        return categoryRepository.save(category);
    }

    @Override
    @Transactional
    public void deleteCategory(UUID id) {

        // search the category by its "id"
        Optional<Category> category = categoryRepository.findById(id);

        // check if the category exists
        if(category.isPresent()) {

            // check if the category has associated posts
            if(!category.get().getPosts().isEmpty()) {
                throw new IllegalArgumentException("Category has posts associated with it.");
            }

            // System.out.println("Deleting category with id: " + id);
            categoryRepository.deleteById(id);
        }
    }
}
