package com.stockpilot.backend.product;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/products")
public class ProductController {

    private final ProductService productService;

    // Spring supplies the service when it creates the controller.
    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    // Return the product list for GET /api/products.
    @GetMapping
    public List<ProductResponse> getProducts() {
        return productService.getProducts();
    }
}
