package com.sauce.api;

import com.sauce.api.models.Product;
import com.sauce.config.Config;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import static org.hamcrest.Matchers.equalTo;

public class ApiClient {
    public Product getProductById(int id) {
        return RestAssured
                .given()
                .baseUri(Config.apiBase())
                .basePath("/products/" + id)
                .header("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36")
                .accept(ContentType.JSON)
                .when()
                .get()
                .then()
                .statusCode(200)
                .body("id", equalTo(id))
                .extract()
                .as(Product.class);
    }
}
