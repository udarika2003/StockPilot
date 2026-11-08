package com.stockpilot.backend.category;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

// Map this Java class to the existing categories table.
@Entity
@Table(name = "categories")
public class Category {

    // PostgreSQL generates the primary key when a category is inserted.
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Map the name field to the required database column.
    @Column(name = "name", nullable = false, length = 120)
    private String name;

    // JPA needs a no-argument constructor to create entity objects.
    protected Category() {
    }

    // Application code uses this constructor to create a new category.
    public Category(String name) {
        this.name = name;
    }

    // Return the category's database-generated ID.
    public Long getId() {
        return id;
    }

    // Return the category name.
    public String getName() {
        return name;
    }
}