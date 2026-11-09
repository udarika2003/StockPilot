package com.stockpilot.backend.product;

import java.math.BigDecimal;

// Define the product fields returned to the frontend.
public record ProductResponse(
        Long id,
        String sku,
        String name,
        Long categoryId,
        String categoryName,
        ProductUnit unit,
        BigDecimal sellingPrice,
        BigDecimal reorderLevel
) {
}