package com.stockpilot.backend.product;

import com.stockpilot.backend.category.Category;
import com.stockpilot.backend.category.CategoryRepository;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Locale;

@Service
public class ProductService {

    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;

    // Use the product repository to save and the category repository to look up categories.
    public ProductService(
            ProductRepository productRepository,
            CategoryRepository categoryRepository
    ) {
        this.productRepository = productRepository;
        this.categoryRepository = categoryRepository;
    }

    @Transactional(readOnly = true)
    public List<ProductResponse> getProducts() {
        return productRepository.findAll(Sort.by("name").ascending())
                .stream()
                .map(this::toResponse)
                .toList();
    }

    @Transactional
    public ProductResponse createProduct(CreateProductRequest request) {
        // Normalize the SKU to match the database format.
        String sku = request.sku().strip().toUpperCase(Locale.ROOT);
        if (sku.length() > 40) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST, "SKU must have at most 40 characters"
            );
        }

        // Products sold by piece or pack require a whole-number reorder level.
        if ((request.unit() == ProductUnit.PIECE || request.unit() == ProductUnit.PACK)
                && request.reorderLevel().stripTrailingZeros().scale() > 0) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST, "Reorder level must be a whole number for PIECE or PACK"
            );
        }

        Category category = categoryRepository.findById(request.categoryId())
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Category not found"
                ));

        Product product = new Product(
                sku,
                request.name().strip(),
                category,
                request.unit(),
                request.sellingPrice(),
                request.reorderLevel()
        );

        try {
            // Execute the insert now so duplicate SKUs can be reported here.
            return toResponse(productRepository.saveAndFlush(product));
        } catch (DataIntegrityViolationException exception) {
            if (exception.getMostSpecificCause() instanceof java.sql.SQLException sqlException
                    && "23505".equals(sqlException.getSQLState())) {
                throw new ResponseStatusException(
                        HttpStatus.CONFLICT, "SKU already exists", exception
                );
            }
            throw exception;
        }
    }

    // Convert the entity into the JSON response shape used by both endpoints.
    private ProductResponse toResponse(Product product) {
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
    }
}