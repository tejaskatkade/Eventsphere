package com.eventsphere.service.impl;

import com.eventsphere.dto.Response.ApiResponse;
import com.eventsphere.entity.Category;
import com.eventsphere.exception.ApiException;
import com.eventsphere.exception.ResourceNotFoundException;
import com.eventsphere.repository.CategoryRepository;
import com.eventsphere.service.CategoryService;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Transactional
public class CategoryServiceImpl implements CategoryService {

    private final CategoryRepository categoryRepository;

    public CategoryServiceImpl(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    @Override
    public ApiResponse createCategory(Category category) {
        if (categoryRepository.existsByName(category.getName()))
            throw new ApiException("Category name :"+ category.getName() +" already exist");

        Category cat = categoryRepository.save(category);
        return new ApiResponse("Created category : " + cat.getName());
    }

    @Override
    public List<Category> getAllCategories() {
        return categoryRepository.findAll();
    }

    @Override
    public Category getCategoryById(Long categoryId) {
        return categoryRepository.findById(categoryId).orElseThrow(() -> new ResourceNotFoundException("Category", categoryId));
    }

    @Override
    public ApiResponse updateCategoryById(Long categoryId, Category newCategory) {
        Category category = categoryRepository.findById(categoryId).orElseThrow(() -> new ResourceNotFoundException("Category", categoryId));

        if ((!category.getName().equalsIgnoreCase(newCategory.getName())) && categoryRepository.existsByName(newCategory.getName()))
            throw new ApiException("Category name :"+ category.getName() +" already exist");

        category.setName(newCategory.getName());
        category.setDescription(newCategory.getDescription());

        Category cat = categoryRepository.save(category);
        return new ApiResponse("Updated category, ID: " + cat.getId());
    }
}
