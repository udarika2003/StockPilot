package com.stockpilot.backend.category;

import java.util.List;

import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

// Handle HTTP requests for category data.
@RestController
@RequestMapping("/api/categories")
public class CategoryController {

    private final CategoryRepository categoryRepository;

    // Spring supplies the repository through constructor injection.
    public CategoryController(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    // Return categories in alphabetical order as response DTOs.
    @GetMapping
    public List<CategoryResponse> getCategories() {
        return categoryRepository.findAll(Sort.by("name").ascending())
                .stream()
                .map(category -> new CategoryResponse(
                        category.getId(),
                        category.getName()
                ))
                .toList();
    }
}