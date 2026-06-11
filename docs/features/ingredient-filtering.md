# Feature: Ingredient filtering

**Status:** implemented

## What it does

On the product list ([[product-list]]), the user can narrow the list by ingredients: "only show
foods that contain X" and "hide foods that contain Y". Useful for avoiding
allergens (no beef) or requiring favorites (must have chicken).

## User experience

1. On the product list page, the user enters one or more **wanted**
   ingredients and/or one or more **unwanted** ingredients.
2. The list updates to show only matching products.
3. Clearing the filters shows the full list again.
4. The filters are part of the page's URL, so a filtered view can be
   bookmarked or shared.

## Rules

- A product is shown only if it contains **every** wanted ingredient **and
  none** of the unwanted ingredients.
- Matching is case-insensitive and matches partial words: "chicken" matches
  "Chicken Meal" and "chicken fat".
- Blank or empty filter entries are ignored; with no filters at all, the full
  list is shown.
- An ingredient can sensibly appear in only one of the two lists; if the same
  term is both wanted and unwanted, no product can match — the result is an
  empty list (not an error).
- No matches is a normal outcome: show an empty list, not an error.

## Out of scope

- Autocomplete / suggesting ingredient names while typing.
- Filtering by anything other than ingredients (brand, pet type, calories).

## Related

- Features: [[product-list]] (the page this filter lives on)
- How it's built: [[backend]] (dynamic `EXISTS` / `NOT EXISTS` SQL) · [[frontend]] (filter inputs, repeated query params)
- Knowledge it exercises: [[sql-and-relational-databases|SQL]] (subqueries) · [[http-and-rest|HTTP & REST]] (query parameters)
