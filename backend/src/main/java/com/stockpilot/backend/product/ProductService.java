package com.stockpilot.backend.product;

import com.stockpilot.backend.category.Category;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class ProductService {

    private final ProductRepository productRepository;

    // Spring supplies the repository when it creates the service.
    public ProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    // Read products and build responses while their categories can still be loaded.
    @Transactional(readOnly = true)
    public List<ProductResponse> getProducts() {
        return productRepository.findAll(Sort.by("name").ascending())
                .stream()
                .map(product -> {
                    Category category = product.getCategory();

                    return new ProductResponse(
                            product.getId(),
                            product.getSku(),
                            product.getName(),
                            category.getId(),
                            category.getName(),
                            product.getUnit(),
                            product.getSellingPrice(),
                            product.getReorderLevel()
                    );
                })
                .toList();
    }
}