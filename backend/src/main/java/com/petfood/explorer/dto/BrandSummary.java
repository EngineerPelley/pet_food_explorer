package com.petfood.explorer.dto;

/** Brand details embedded in a product detail response. */
public record BrandSummary(
        long brandId,
        String name,
        String description) {
}
