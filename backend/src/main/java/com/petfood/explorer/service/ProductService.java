package com.petfood.explorer.service;

import com.petfood.explorer.error.NotFoundException;
import com.petfood.explorer.repository.ProductRepository;
import com.petfood.explorer.view.ProductDetail;
import com.petfood.explorer.view.ProductSummary;
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

    /**
     * Lists products, optionally filtered by ingredients to include
     * ({@code wanted}) and exclude ({@code unwanted}). Null lists mean "no
     * filter on that side".
     */
    public List<ProductSummary> listProducts(List<String> wanted, List<String> unwanted) {
        return repository.search(wanted, unwanted);
    }

    public ProductDetail getProduct(long id) {
        return repository.findById(id)
                .orElseThrow(() -> new NotFoundException("No product found with id " + id));
    }
}
