package com.petfood.explorer.controller;

import com.petfood.explorer.service.ProductService;
import com.petfood.explorer.view.ProductDetail;
import com.petfood.explorer.view.ProductSummary;
import java.util.List;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
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

    /**
     * GET /api/products -- list products, optionally filtered by ingredients.
     *
     * <p>Both params are optional and repeatable, e.g.
     * {@code /api/products?wanted=chicken&wanted=rice&unwanted=beef}. A food is
     * kept only if it contains every {@code wanted} ingredient and none of the
     * {@code unwanted} ones. With no params, all products are returned.
     */
    @GetMapping
    public List<ProductSummary> list(
            @RequestParam(required = false) List<String> wanted,
            @RequestParam(required = false) List<String> unwanted) {
        return service.listProducts(wanted, unwanted);
    }

    /** GET /api/products/{id} -- full detail for one product. */
    @GetMapping("/{id}")
    public ProductDetail get(@PathVariable long id) {
        return service.getProduct(id);
    }
}
