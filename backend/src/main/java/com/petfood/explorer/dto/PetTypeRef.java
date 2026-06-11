package com.petfood.explorer.dto;

/** Pet type a food is intended for (dog, cat). */
public record PetTypeRef(
        long petTypeId,
        String name) {
}
