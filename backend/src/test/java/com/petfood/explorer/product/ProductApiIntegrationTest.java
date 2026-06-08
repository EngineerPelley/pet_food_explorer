package com.petfood.explorer.product;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;

/**
 * Boots the full application context against the local petfood_test database
 * (Flyway applies the schema + seed data) and exercises the product endpoints
 * over HTTP.
 */
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
class ProductApiIntegrationTest {

    @Autowired
    private TestRestTemplate rest;

    @Test
    void listProductsReturnsSeededRows() {
        ResponseEntity<ProductSummary[]> response =
                rest.getForEntity("/api/products", ProductSummary[].class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
        // V2 seeds 12 foods.
        assertThat(response.getBody()).hasSize(12);
        assertThat(response.getBody()[0].brandName()).isNotBlank();
    }

    @Test
    void productDetailReturnsRelatedData() {
        ResponseEntity<ProductDetail> response =
                rest.getForEntity("/api/products/1", ProductDetail.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        ProductDetail detail = response.getBody();
        assertThat(detail).isNotNull();
        assertThat(detail.foodId()).isEqualTo(1L);
        assertThat(detail.brand().name()).isEqualTo("Acme Pet Co");
        assertThat(detail.ingredients()).isNotEmpty();
        assertThat(detail.petTypes()).isNotEmpty();
    }

    @Test
    void unknownProductReturns404() {
        ResponseEntity<String> response =
                rest.getForEntity("/api/products/999999", String.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    }
}
