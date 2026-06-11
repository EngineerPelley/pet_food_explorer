# Feature: Product detail

**Status:** implemented

## What it does

A page showing everything known about one pet food product, reached by
clicking it in the product list.

## User experience

1. The user clicks a product in the list (or opens its link directly —
   each product has its own URL that can be bookmarked or shared).
2. The page shows:
   - the product's name, description, and calories per cup;
   - its **brand**, with the brand's description;
   - the kind of product (dry, wet, ...), if it has one;
   - which **pets** it is for (dog, cat — can be both), listed alphabetically;
   - its **ingredients**, in the same order they appear on the package label
     (heaviest first), each with its source (animal / plant / synthetic) and,
     when known, the percentage it makes up.
3. A link takes the user back to the product list.

## Rules

- Brand is always present; product kind, description, calories, ingredient
  percentages, and sources may be missing — the page handles each gracefully.
- Ingredients are ordered by their position on the label.
- Opening a URL for a product that doesn't exist shows a clear "not found"
  message, not a crash or a blank page.

## Out of scope

- Sub-ingredients (what a compound ingredient like "chicken meal" contains) —
  the data model supports them, but they are not shown yet.
- Editing anything; the app is read-only.
