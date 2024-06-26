package com.marco.productservice;

import io.restassured.RestAssured;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.testcontainers.containers.MongoDBContainer;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class ProductServiceApplicationTests {

    @ServiceConnection
    static MongoDBContainer mongoDBContainer = new MongoDBContainer("mongo:7.0.5");
    @LocalServerPort
    private Integer port;

    @BeforeEach
    void setup(){
        RestAssured.baseURI = "http://localhost";
        RestAssured.port = port;
    }

    static {
        mongoDBContainer.start();
    }

    @Test
    void shouldCreateProduct() {
        String requestBody = """
                {
                    "name": "iPhone 15",
                    "description": "iPhone 15 is a smartphone by Apple",
                    "price": 1000
                }
                """;
        RestAssured.given()
                .contentType("application/Json")
                .body(requestBody)
                .when()
                .post("/api/products")
                .then()
                .statusCode(201)
                .body("id", Matchers.notNullValue())
                .body("name", Matchers.equalTo("iPhone 15"))
                .body("description", Matchers.equalTo("iPhone 15 is a smartphone by Apple"))
                .body("price", Matchers.equalTo(1000));
    }

    @Test
    void shouldReturnProducts(){
        String requestBody1 = """
                {
                    "name": "iPhone 15",
                    "description": "iPhone 15 is a smartphone by Apple",
                    "price": 1000
                }
                """;

        RestAssured.given()
                .contentType("application/Json")
                .body(requestBody1)
                .when()
                .post("/api/products")
                .then()
                .statusCode(201);

        RestAssured.given()
                //.contentType("application/json")
                .when()
                .get("/api/products")
                .then()
                .statusCode(200)
                .body("", Matchers.hasSize(Matchers.greaterThan(0)))
                .body("name", Matchers.hasItem("iPhone 15"))
                .body("description", Matchers.hasItem("iPhone 15 is a smartphone by Apple"))
                .body("price", Matchers.hasItem(1000));
    }

}
