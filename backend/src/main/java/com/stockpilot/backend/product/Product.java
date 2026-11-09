package com.stockpilot.backend.product;

import com.stockpilot.backend.category.Category;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

import java.math.BigDecimal;

// Map this entity to the products table created by Flyway.
@Entity
@Table(name = "products")
public class Product {

    // PostgreSQL generates the ID when the product is saved.
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Store the unique stock code used to identify the product.
    @Column(nullable = false, length = 40, unique = true)
    private String sku;

    @Column(nullable = false, length = 120)
    private String name;

    // Link this product to its category through products.category_id.
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "category_id", nullable = false)
    private Category category;

    // Store the enum name, such as KG, in the text column.
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 10)
    private ProductUnit unit;

    // Use exact decimal values for money and stock quantities.
    @Column(name = "selling_price", nullable = false, precision = 12, scale = 2)
    private BigDecimal sellingPrice;

    @Column(name = "reorder_level", nullable = false, precision = 12, scale = 3)
    private BigDecimal reorderLevel;

    // JPA uses this constructor when loading a product from the database.
    protected Product() {
    }

    // Build a new product with the category found by the service.
    public Product(
            String sku,
            String name,
            Category category,
            ProductUnit unit,
            BigDecimal sellingPrice,
            BigDecimal reorderLevel
    ) {
        this.sku = sku;
        this.name = name;
        this.category = category;
        this.unit = unit;
        this.sellingPrice = sellingPrice;
        this.reorderLevel = reorderLevel;
    }

    // Expose the product fields used to build the API response.
    public Long getId() {
        return id;
    }

    public String getSku() {
        return sku;
    }

    public String getName() {
        return name;
    }

    public Category getCategory() {
        return category;
    }

    public ProductUnit getUnit() {
        return unit;
    }

    public BigDecimal getSellingPrice() {
        return sellingPrice;
    }

    public BigDecimal getReorderLevel() {
        return reorderLevel;
    }
}
