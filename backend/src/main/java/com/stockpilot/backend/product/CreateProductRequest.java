package com.stockpilot.backend.product;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

import java.math.BigDecimal;

// Define and validate the fields clients send to create a product.
public record CreateProductRequest(
        @NotBlank @Size(max = 40) String sku,
        @NotBlank @Size(max = 120) String name,
        @NotNull @Positive Long categoryId,
        @NotNull ProductUnit unit,
        @NotNull @DecimalMin(value = "0", inclusive = false)
        @Digits(integer = 10, fraction = 2) BigDecimal sellingPrice,
        @NotNull @DecimalMin("0")
        @Digits(integer = 9, fraction = 3) BigDecimal reorderLevel
) {
}