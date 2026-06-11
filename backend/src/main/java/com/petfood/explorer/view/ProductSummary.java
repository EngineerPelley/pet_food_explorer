package com.petfood.explorer.view;

import java.math.BigDecimal;

/**
 * Row in the product (food) list. Brand and product-type names are joined in so
 * the list view needs no further lookups.
 */
public record ProductSummary(
        long foodId,
        String name,
        String brandName,
        String productTypeName,
        BigDecimal caloriesPerCup) {
}
