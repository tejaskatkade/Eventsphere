package com.eventsphere.service;

import com.eventsphere.dto.Response.ApiResponse;
import com.eventsphere.entity.Category;

import java.util.List;

public interface CategoryService {

    ApiResponse createCategory(Category category);

    List<Category> getAllCategories();

    Category getCategoryById(Long categoryId);

    ApiResponse updateCategoryById(Long categoryId, Category newCategory);
}
