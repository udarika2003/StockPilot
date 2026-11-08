package com.stockpilot.backend.category;

import org.springframework.data.jpa.repository.JpaRepository;

// Provide database operations for Category entities.
// Category is the entity type, and Long is its primary key type.
public interface CategoryRepository extends JpaRepository<Category, Long> {
}