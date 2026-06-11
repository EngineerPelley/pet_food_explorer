package com.petfood.explorer.dto;

/**
 * Product type reference (dry, wet, raw, ...). Nullable on a food, so this may be
 * absent from a product detail response.
 */
public record ProductTypeRef(
        long productTypeId,
        String name) {
}
