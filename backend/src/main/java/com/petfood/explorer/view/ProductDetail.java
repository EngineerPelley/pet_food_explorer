package com.petfood.explorer.view;

import com.petfood.explorer.dto.BrandSummary;
import com.petfood.explorer.dto.IngredientView;
import com.petfood.explorer.dto.PetTypeRef;
import com.petfood.explorer.dto.ProductTypeRef;
import java.math.BigDecimal;
import java.util.List;

/**
 * Full product (food) detail: the food itself plus its brand, optional product
 * type, the pet types it targets, and its label ingredients.
 */
public record ProductDetail(
        long foodId,
        String name,
        String description,
        BigDecimal caloriesPerCup,
        BrandSummary brand,
        ProductTypeRef productType,
        List<PetTypeRef> petTypes,
        List<IngredientView> ingredients) {
}
