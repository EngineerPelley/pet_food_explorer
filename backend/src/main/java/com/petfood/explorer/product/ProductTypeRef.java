package com.petfood.explorer.product;

/**
 * Product type reference (dry, wet, raw, ...). Nullable on a food, so this may be
 * absent from a product detail response.
 */
public record ProductTypeRef(
        long productTypeId,
        String name) {
}
