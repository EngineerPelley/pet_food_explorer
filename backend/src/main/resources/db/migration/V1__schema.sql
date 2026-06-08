-- V1__schema.sql
-- Pet Food schema (MySQL / InnoDB).
--
-- This is the data model adapted for Flyway: the database itself (`petfood`) is
-- created out-of-band (see README) and selected via the JDBC URL, so there is no
-- CREATE DATABASE / USE / DROP here -- Flyway manages versioning instead.

-- ---------------------------------------------------------------------------
-- Lookup / parent tables
-- ---------------------------------------------------------------------------

CREATE TABLE brand (
    brand_id    INT UNSIGNED NOT NULL AUTO_INCREMENT,
    name        VARCHAR(150) NOT NULL,
    description TEXT NULL,
    PRIMARY KEY (brand_id),
    UNIQUE KEY uq_brand_name (name)
) ENGINE = InnoDB DEFAULT CHARSET = utf8mb4 COLLATE = utf8mb4_unicode_ci;

CREATE TABLE product_type (
    product_type_id INT UNSIGNED NOT NULL AUTO_INCREMENT,
    name            VARCHAR(50) NOT NULL COMMENT 'dry, wet, raw, freeze-dried, treat',
    PRIMARY KEY (product_type_id),
    UNIQUE KEY uq_product_type_name (name)
) ENGINE = InnoDB DEFAULT CHARSET = utf8mb4 COLLATE = utf8mb4_unicode_ci;

CREATE TABLE pet_type (
    pet_type_id INT UNSIGNED NOT NULL AUTO_INCREMENT,
    name        VARCHAR(50) NOT NULL COMMENT 'dog, cat',
    PRIMARY KEY (pet_type_id),
    UNIQUE KEY uq_pet_type_name (name)
) ENGINE = InnoDB DEFAULT CHARSET = utf8mb4 COLLATE = utf8mb4_unicode_ci;

CREATE TABLE ingredient (
    ingredient_id INT UNSIGNED NOT NULL AUTO_INCREMENT,
    name          VARCHAR(150) NOT NULL,
    source        VARCHAR(50) NULL COMMENT 'animal, plant, synthetic',
    PRIMARY KEY (ingredient_id),
    UNIQUE KEY uq_ingredient_name (name)
) ENGINE = InnoDB DEFAULT CHARSET = utf8mb4 COLLATE = utf8mb4_unicode_ci;

-- ---------------------------------------------------------------------------
-- Core tables
-- ---------------------------------------------------------------------------

CREATE TABLE food (
    food_id          INT UNSIGNED NOT NULL AUTO_INCREMENT,
    brand_id         INT UNSIGNED NOT NULL,
    product_type_id  INT UNSIGNED NULL,
    name             VARCHAR(200) NOT NULL,
    description      TEXT NULL,
    calories_per_cup DECIMAL(6,2) NULL,
    PRIMARY KEY (food_id),
    KEY idx_food_brand (brand_id),
    KEY idx_food_product_type (product_type_id),
    CONSTRAINT fk_food_brand
        FOREIGN KEY (brand_id) REFERENCES brand (brand_id)
        ON DELETE RESTRICT ON UPDATE CASCADE,
    CONSTRAINT fk_food_product_type
        FOREIGN KEY (product_type_id) REFERENCES product_type (product_type_id)
        ON DELETE SET NULL ON UPDATE CASCADE
) ENGINE = InnoDB DEFAULT CHARSET = utf8mb4 COLLATE = utf8mb4_unicode_ci;

CREATE TABLE sub_ingredient (
    sub_ingredient_id INT UNSIGNED NOT NULL AUTO_INCREMENT,
    ingredient_id     INT UNSIGNED NOT NULL,
    name              VARCHAR(150) NOT NULL,
    PRIMARY KEY (sub_ingredient_id),
    KEY idx_sub_ingredient_parent (ingredient_id),
    CONSTRAINT fk_sub_ingredient_ingredient
        FOREIGN KEY (ingredient_id) REFERENCES ingredient (ingredient_id)
        ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE = InnoDB DEFAULT CHARSET = utf8mb4 COLLATE = utf8mb4_unicode_ci;

-- ---------------------------------------------------------------------------
-- Junction tables (many-to-many)
-- ---------------------------------------------------------------------------

CREATE TABLE food_ingredient (
    food_id        INT UNSIGNED NOT NULL,
    ingredient_id  INT UNSIGNED NOT NULL,
    label_position INT UNSIGNED NULL COMMENT 'order by weight on label',
    percentage     DECIMAL(5,2) NULL,
    PRIMARY KEY (food_id, ingredient_id),
    KEY idx_fi_ingredient (ingredient_id),
    CONSTRAINT fk_fi_food
        FOREIGN KEY (food_id) REFERENCES food (food_id)
        ON DELETE CASCADE ON UPDATE CASCADE,
    CONSTRAINT fk_fi_ingredient
        FOREIGN KEY (ingredient_id) REFERENCES ingredient (ingredient_id)
        ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE = InnoDB DEFAULT CHARSET = utf8mb4 COLLATE = utf8mb4_unicode_ci;

CREATE TABLE food_pet_type (
    food_id     INT UNSIGNED NOT NULL,
    pet_type_id INT UNSIGNED NOT NULL,
    PRIMARY KEY (food_id, pet_type_id),
    KEY idx_fpt_pet_type (pet_type_id),
    CONSTRAINT fk_fpt_food
        FOREIGN KEY (food_id) REFERENCES food (food_id)
        ON DELETE CASCADE ON UPDATE CASCADE,
    CONSTRAINT fk_fpt_pet_type
        FOREIGN KEY (pet_type_id) REFERENCES pet_type (pet_type_id)
        ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE = InnoDB DEFAULT CHARSET = utf8mb4 COLLATE = utf8mb4_unicode_ci;
