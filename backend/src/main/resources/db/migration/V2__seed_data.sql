-- V2__seed_data.sql
-- Hand-created sample data. Explicit primary-key values are used throughout so
-- the foreign keys in the junction tables are easy to follow and edit by hand.

-- ---------------------------------------------------------------------------
-- Lookup tables
-- ---------------------------------------------------------------------------

INSERT INTO brand (brand_id, name, description) VALUES
    (1, 'Acme Pet Co',     'Everyday kibble and canned food for dogs and cats.'),
    (2, 'Wholesome Paws',  'Limited-ingredient recipes focused on whole foods.'),
    (3, 'Wild Range',      'High-protein, grain-free formulas inspired by ancestral diets.'),
    (4, 'Ocean Bowl',      'Fish-forward recipes rich in omega fatty acids.'),
    (5, 'Green Pastures',  'Plant-balanced recipes with sustainably sourced meat.'),
    (6, 'Feline Fine',     'Cat-only specialists in wet and freeze-dried food.'),
    (7, 'Pup Kitchen',     'Small-batch fresh and raw meals for dogs.');

INSERT INTO product_type (product_type_id, name) VALUES
    (1, 'dry'),
    (2, 'wet'),
    (3, 'raw'),
    (4, 'freeze-dried'),
    (5, 'treat');

INSERT INTO pet_type (pet_type_id, name) VALUES
    (1, 'dog'),
    (2, 'cat');

INSERT INTO ingredient (ingredient_id, name, source) VALUES
    (1,  'Chicken',       'animal'),
    (2,  'Beef',          'animal'),
    (3,  'Salmon',        'animal'),
    (4,  'Turkey',        'animal'),
    (5,  'Lamb',          'animal'),
    (6,  'Brown Rice',    'plant'),
    (7,  'Sweet Potato',  'plant'),
    (8,  'Peas',          'plant'),
    (9,  'Carrots',       'plant'),
    (10, 'Pumpkin',       'plant'),
    (11, 'Flaxseed',      'plant'),
    (12, 'Blueberries',   'plant'),
    (13, 'Taurine',       'synthetic');

-- A few ingredients break down into more specific sub-ingredients.
INSERT INTO sub_ingredient (sub_ingredient_id, ingredient_id, name) VALUES
    (1, 1, 'Chicken Meal'),
    (2, 1, 'Chicken Fat'),
    (3, 3, 'Salmon Oil'),
    (4, 2, 'Beef Liver'),
    (5, 4, 'Turkey Meal');

-- ---------------------------------------------------------------------------
-- Foods (the "products" browsed in the app)
-- ---------------------------------------------------------------------------

INSERT INTO food (food_id, brand_id, product_type_id, name, description, calories_per_cup) VALUES
    (1,  1, 1, 'Acme Classic Chicken & Rice',       'Balanced everyday dry food for adult dogs.',                 365.00),
    (2,  1, 2, 'Acme Hearty Beef Stew',             'Canned wet food with beef and garden vegetables.',           220.50),
    (3,  2, 1, 'Wholesome Paws Lamb & Sweet Potato','Limited-ingredient grain-inclusive kibble.',                 380.00),
    (4,  2, 5, 'Wholesome Paws Pumpkin Bites',      'Soft baked treats with pumpkin and flaxseed.',               45.00),
    (5,  3, 1, 'Wild Range High-Protein Turkey',    'Grain-free dry food with 38% protein.',                      410.25),
    (6,  3, 3, 'Wild Range Raw Beef Patties',       'Frozen raw patties for a biologically appropriate diet.',    520.00),
    (7,  4, 2, 'Ocean Bowl Salmon Pate',            'Smooth salmon pate for cats, rich in omega-3.',              95.75),
    (8,  4, 1, 'Ocean Bowl Salmon & Pea Kibble',    'Fish-first dry food for dogs with sensitive skin.',          372.00),
    (9,  5, 1, 'Green Pastures Turkey & Veggie',    'Plant-balanced kibble with sustainably raised turkey.',      350.00),
    (10, 6, 4, 'Feline Fine Freeze-Dried Chicken',  'Single-protein freeze-dried raw food for cats.',             480.00),
    (11, 6, 2, 'Feline Fine Tuna Feast',            'Wet food featuring wild-caught fish and added taurine.',     90.00),
    (12, 7, 3, 'Pup Kitchen Fresh Beef Bowl',       'Gently cooked fresh beef meal delivered cold.',              430.00);

-- ---------------------------------------------------------------------------
-- food_ingredient: ingredients per food, ordered by label_position
-- (label_position = order by weight on the label; percentage is optional).
-- ---------------------------------------------------------------------------

INSERT INTO food_ingredient (food_id, ingredient_id, label_position, percentage) VALUES
    -- 1: Acme Classic Chicken & Rice
    (1, 1, 1, 32.00), (1, 6, 2, 28.00), (1, 9, 3, 10.00),
    -- 2: Acme Hearty Beef Stew
    (2, 2, 1, 40.00), (2, 9, 2, 12.00), (2, 8, 3, 8.00),
    -- 3: Wholesome Paws Lamb & Sweet Potato
    (3, 5, 1, 30.00), (3, 7, 2, 25.00), (3, 11, 3, 5.00),
    -- 4: Wholesome Paws Pumpkin Bites
    (4, 10, 1, 35.00), (4, 11, 2, 10.00),
    -- 5: Wild Range High-Protein Turkey
    (5, 4, 1, 38.00), (5, 8, 2, 18.00), (5, 12, 3, 4.00),
    -- 6: Wild Range Raw Beef Patties
    (6, 2, 1, 70.00), (6, 9, 2, 6.00),
    -- 7: Ocean Bowl Salmon Pate
    (7, 3, 1, 45.00), (7, 13, 2, 0.10),
    -- 8: Ocean Bowl Salmon & Pea Kibble
    (8, 3, 1, 34.00), (8, 8, 2, 20.00), (8, 7, 3, 12.00),
    -- 9: Green Pastures Turkey & Veggie
    (9, 4, 1, 26.00), (9, 8, 2, 16.00), (9, 9, 3, 12.00), (9, 12, 4, 3.00),
    -- 10: Feline Fine Freeze-Dried Chicken
    (10, 1, 1, 98.00), (10, 13, 2, 0.20),
    -- 11: Feline Fine Tuna Feast
    (11, 3, 1, 42.00), (11, 13, 2, 0.15),
    -- 12: Pup Kitchen Fresh Beef Bowl
    (12, 2, 1, 55.00), (12, 7, 2, 15.00), (12, 10, 3, 8.00);

-- ---------------------------------------------------------------------------
-- food_pet_type: which pets each food is intended for
-- ---------------------------------------------------------------------------

INSERT INTO food_pet_type (food_id, pet_type_id) VALUES
    (1, 1),         -- dog
    (2, 1),         -- dog
    (3, 1),         -- dog
    (4, 1),         -- dog
    (5, 1),         -- dog
    (6, 1),         -- dog
    (7, 2),         -- cat
    (8, 1),         -- dog
    (9, 1), (9, 2), -- dog & cat
    (10, 2),        -- cat
    (11, 2),        -- cat
    (12, 1);        -- dog
