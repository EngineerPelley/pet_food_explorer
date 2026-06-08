package com.petfood.explorer.product;

import java.util.List;
import java.util.Optional;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.stereotype.Repository;

/**
 * Data access for products (the {@code food} table) using Spring's
 * {@link JdbcClient} and hand-written SQL.
 *
 * <p>Conventions, by design (this project is partly about learning SQL):
 * <ul>
 *   <li>Every query lists its columns explicitly -- no {@code SELECT *}.</li>
 *   <li>Parameters are bound by name -- never string concatenation.</li>
 *   <li>Rows map to plain Java records via small lambda row mappers.</li>
 * </ul>
 */
@Repository
public class ProductRepository {

    private final JdbcClient jdbc;

    public ProductRepository(JdbcClient jdbc) {
        this.jdbc = jdbc;
    }

    // -- Product list ------------------------------------------------------

    // Join each food to its brand (required) and product type (optional, hence
    // LEFT JOIN) so the list can show names rather than raw ids.
    private static final String FIND_ALL = """
            SELECT f.food_id,
                   f.name,
                   b.name  AS brand_name,
                   pt.name AS product_type_name,
                   f.calories_per_cup
            FROM food f
            JOIN brand b              ON b.brand_id = f.brand_id
            LEFT JOIN product_type pt ON pt.product_type_id = f.product_type_id
            ORDER BY f.name
            """;

    public List<ProductSummary> findAll() {
        return jdbc.sql(FIND_ALL)
                .query((rs, rowNum) -> new ProductSummary(
                        rs.getLong("food_id"),
                        rs.getString("name"),
                        rs.getString("brand_name"),
                        rs.getString("product_type_name"),
                        rs.getBigDecimal("calories_per_cup")))
                .list();
    }

    // -- Product detail ----------------------------------------------------

    // The food plus its brand and (optional) product type. product_type columns
    // can be NULL because of the LEFT JOIN / nullable FK.
    private static final String FIND_BY_ID = """
            SELECT f.food_id,
                   f.name,
                   f.description,
                   f.calories_per_cup,
                   b.brand_id,
                   b.name        AS brand_name,
                   b.description AS brand_description,
                   pt.product_type_id,
                   pt.name       AS product_type_name
            FROM food f
            JOIN brand b              ON b.brand_id = f.brand_id
            LEFT JOIN product_type pt ON pt.product_type_id = f.product_type_id
            WHERE f.food_id = :id
            """;

    // Pet types this food targets, via the food_pet_type junction table.
    private static final String FIND_PET_TYPES = """
            SELECT pt.pet_type_id,
                   pt.name
            FROM food_pet_type fpt
            JOIN pet_type pt ON pt.pet_type_id = fpt.pet_type_id
            WHERE fpt.food_id = :id
            ORDER BY pt.name
            """;

    // Label ingredients, ordered by their position on the label. The junction
    // table (food_ingredient) carries label_position and percentage.
    private static final String FIND_INGREDIENTS = """
            SELECT i.ingredient_id,
                   i.name,
                   i.source,
                   fi.label_position,
                   fi.percentage
            FROM food_ingredient fi
            JOIN ingredient i ON i.ingredient_id = fi.ingredient_id
            WHERE fi.food_id = :id
            ORDER BY fi.label_position
            """;

    /**
     * Loads a full product detail with one query per related collection. Returns
     * empty if no food has the given id.
     */
    public Optional<ProductDetail> findById(long id) {
        Optional<ProductDetail> base = jdbc.sql(FIND_BY_ID)
                .param("id", id)
                .query((rs, rowNum) -> {
                    // product_type_id is read as a primitive then corrected for
                    // SQL NULL via wasNull(), so an absent type maps to null.
                    long productTypeId = rs.getLong("product_type_id");
                    ProductTypeRef productType = rs.wasNull()
                            ? null
                            : new ProductTypeRef(productTypeId, rs.getString("product_type_name"));

                    BrandSummary brand = new BrandSummary(
                            rs.getLong("brand_id"),
                            rs.getString("brand_name"),
                            rs.getString("brand_description"));

                    return new ProductDetail(
                            rs.getLong("food_id"),
                            rs.getString("name"),
                            rs.getString("description"),
                            rs.getBigDecimal("calories_per_cup"),
                            brand,
                            productType,
                            List.of(),   // filled in below
                            List.of());  // filled in below
                })
                .optional();

        if (base.isEmpty()) {
            return Optional.empty();
        }

        List<PetTypeRef> petTypes = jdbc.sql(FIND_PET_TYPES)
                .param("id", id)
                .query((rs, rowNum) -> new PetTypeRef(
                        rs.getLong("pet_type_id"),
                        rs.getString("name")))
                .list();

        List<IngredientView> ingredients = jdbc.sql(FIND_INGREDIENTS)
                .param("id", id)
                .query((rs, rowNum) -> {
                    Integer labelPosition = rs.getObject("label_position", Integer.class);
                    return new IngredientView(
                            rs.getLong("ingredient_id"),
                            rs.getString("name"),
                            rs.getString("source"),
                            labelPosition,
                            rs.getBigDecimal("percentage"));
                })
                .list();

        ProductDetail d = base.get();
        return Optional.of(new ProductDetail(
                d.foodId(), d.name(), d.description(), d.caloriesPerCup(),
                d.brand(), d.productType(), petTypes, ingredients));
    }
}
