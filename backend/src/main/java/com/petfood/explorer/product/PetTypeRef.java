package com.petfood.explorer.product;

/** Pet type a food is intended for (dog, cat). */
public record PetTypeRef(
        long petTypeId,
        String name) {
}
