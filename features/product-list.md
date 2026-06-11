# Feature: Product list

**Status:** implemented

## What it does

The home page shows a list of all pet food products so the user can browse
what exists and pick one to look at in detail.

## User experience

1. The user opens the site and lands on the product list.
2. Each product shows its name, the brand that makes it, the kind of product
   it is (dry, wet, treat, ...), and calories per cup.
3. Products are listed alphabetically by name.
4. Clicking a product takes the user to its detail page
   ([product-detail.md](product-detail.md)).

## Rules

- Every product always has a brand to show.
- The product kind is optional — some products don't have one, and the list
  still shows them (with that column simply empty).
- Calories per cup is optional and may be empty.

## Out of scope

- Paging or limiting the list (the catalog is small).
- Searching by name or sorting by other columns.
- Filtering — added later as its own feature
  ([ingredient-filtering.md](ingredient-filtering.md)).
