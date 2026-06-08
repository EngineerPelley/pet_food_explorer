package com.petfood.explorer.product;

import com.petfood.explorer.web.NotFoundException;
import java.util.List;
import org.springframework.stereotype.Service;

/**
 * Application logic for products. Thin for now, but the layer is where any
 * business rules would live as the app grows.
 */
@Service
public class ProductService {

    private final ProductRepository repository;

    public ProductService(ProductRepository repository) {
        this.repository = repository;
    }

    public List<ProductSummary> listProducts() {
        return repository.findAll();
    }

    public ProductDetail getProduct(long id) {
        return repository.findById(id)
                .orElseThrow(() -> new NotFoundException("No product found with id " + id));
    }
}
