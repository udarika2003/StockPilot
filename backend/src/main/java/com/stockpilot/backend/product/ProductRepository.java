package com.stockpilot.backend.product;

import org.springframework.data.jpa.repository.JpaRepository;

// Provide database operations for Product entities with Long IDs.
public interface ProductRepository extends JpaRepository<Product, Long> {
}
