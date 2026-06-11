package com.petfood.explorer.dto;

import java.math.BigDecimal;

/**
 * An ingredient as it appears on a food's label, including its position
 * (order by weight) and optional percentage.
 */
public record IngredientView(
        long ingredientId,
        String name,
        String source,
        Integer labelPosition,
        BigDecimal percentage) {
}
