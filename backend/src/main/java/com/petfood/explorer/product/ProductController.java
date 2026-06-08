package com.petfood.explorer.product;

import java.util.List;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * REST endpoints for products (foods). "Product" is the public-facing name for a
 * row in the {@code food} table.
 */
@RestController
@RequestMapping("/api/products")
public class ProductController {

    private final ProductService service;

    public ProductController(ProductService service) {
        this.service = service;
    }

    /** GET /api/products -- list all products. */
    @GetMapping
    public List<ProductSummary> list() {
        return service.listProducts();
    }

    /** GET /api/products/{id} -- full detail for one product. */
    @GetMapping("/{id}")
    public ProductDetail get(@PathVariable long id) {
        return service.getProduct(id);
    }
}
