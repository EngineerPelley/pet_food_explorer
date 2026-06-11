package com.petfood.explorer.repository;

import com.petfood.explorer.dto.BrandSummary;
import com.petfood.explorer.dto.IngredientView;
import com.petfood.explorer.dto.PetTypeRef;
import com.petfood.explorer.dto.ProductTypeRef;
import com.petfood.explorer.view.ProductDetail;
import com.petfood.explorer.view.ProductSummary;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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

    // -- Product list (with optional ingredient filtering) -----------------

    // Base list query: each food with its brand (required) and product type
    // (optional, hence LEFT JOIN) so the list shows names, not raw ids. The
    // WHERE conditions and ORDER BY are appended dynamically by search().
    private static final String FIND_ALL_BASE = """
            SELECT f.food_id,
                   f.name,
                   b.name  AS brand_name,
                   pt.name AS product_type_name,
                   f.calories_per_cup
            FROM food f
            JOIN brand b              ON b.brand_id = f.brand_id
            LEFT JOIN product_type pt ON pt.product_type_id = f.product_type_id
            """;

    /**
     * Lists products, optionally filtered by ingredients the food must contain
     * ({@code wanted}) and ingredients it must not contain ({@code unwanted}).
     *
     * <p>Each wanted term adds a correlated {@code EXISTS} subquery -- so a food
     * must match <em>every</em> wanted term -- and each unwanted term adds a
     * {@code NOT EXISTS} subquery -- so a food matching <em>any</em> unwanted
     * term is excluded. Terms match case-insensitively as substrings (LIKE).
     * Null/blank terms are dropped, so "no filter" returns the full list.
     */
    public List<ProductSummary> search(List<String> wanted, List<String> unwanted) {
        StringBuilder sql = new StringBuilder(FIND_ALL_BASE);
        Map<String, Object> params = new HashMap<>();

        // WHERE 1 = 1 lets every optional condition below be appended uniformly
        // with "AND ...", with no special-casing of the first one.
        sql.append("WHERE 1 = 1\n");

        int i = 0;
        for (String term : clean(wanted)) {
            String key = "w" + i++;
            // Keep the food only if it has an ingredient matching this term.
            sql.append("""
                    AND EXISTS (
                        SELECT 1
                        FROM food_ingredient fi
                        JOIN ingredient ing ON ing.ingredient_id = fi.ingredient_id
                        WHERE fi.food_id = f.food_id AND ing.name LIKE :%s
                    )
                    """.formatted(key));
            params.put(key, "%" + term + "%");
        }

        int j = 0;
        for (String term : clean(unwanted)) {
            String key = "u" + j++;
            // Drop the food if it has any ingredient matching this term.
            sql.append("""
                    AND NOT EXISTS (
                        SELECT 1
                        FROM food_ingredient fi
                        JOIN ingredient ing ON ing.ingredient_id = fi.ingredient_id
                        WHERE fi.food_id = f.food_id AND ing.name LIKE :%s
                    )
                    """.formatted(key));
            params.put(key, "%" + term + "%");
        }

        sql.append("ORDER BY f.name");

        return jdbc.sql(sql.toString())
                .params(params)
                .query((rs, rowNum) -> new ProductSummary(
                        rs.getLong("food_id"),
                        rs.getString("name"),
                        rs.getString("brand_name"),
                        rs.getString("product_type_name"),
                        rs.getBigDecimal("calories_per_cup")))
                .list();
    }

    /** Drops null/blank terms and trims the rest; a null list becomes empty. */
    private static List<String> clean(List<String> terms) {
        if (terms == null) {
            return List.of();
        }
        return terms.stream()
                .filter(t -> t != null && !t.isBlank())
                .map(String::trim)
                .toList();
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
