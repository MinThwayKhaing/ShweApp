package com.app.shwe.service;

import com.app.shwe.model.Category;
import com.app.shwe.repository.CategoryRepository;

import jakarta.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CategoryService {

    @Autowired
    private CategoryRepository categoryRepository;

    public List<Category> getAllCategories() {
        return categoryRepository.findAllCategories();
    }

    public Optional<Category> getCategoryById(Long id) {
        if (id == null) {
            throw new IllegalArgumentException("Id cannot be null");
        }
        return categoryRepository.findCategoryById(id);
    }

    @Transactional
    public Category addCategory(Category category) {
        if (category == null) {
            throw new IllegalArgumentException("Insert Category cannot be null");
        }
        categoryRepository.insertCategory(category.getName(), category.getLabel());
        // Assuming you want to return the inserted category object, you may need to fetch it again from the database.
        // This is a workaround because native queries don't return the entity.
        return category;
    }

    @Transactional
    public Category updateCategory(Long id, Category categoryDetails) {
        if (categoryDetails == null) {
            throw new IllegalArgumentException("Update Category cannot be null");
        }
        categoryRepository.findCategoryById(id).orElseThrow(() -> new RuntimeException("Category not found"));
        categoryRepository.updateCategory(id, categoryDetails.getName(), categoryDetails.getLabel());
        return categoryDetails;
    }

    @Transactional
    public void deleteCategory(Long id) {
        if (id == null) {
            throw new IllegalArgumentException("Delete ID cannot be null");
        }
        categoryRepository.deleteCategoryById(id);
    }
}
