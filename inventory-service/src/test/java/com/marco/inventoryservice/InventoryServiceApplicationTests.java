package com.marco.inventoryservice;

import io.restassured.RestAssured;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.testcontainers.containers.MySQLContainer;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class InventoryServiceApplicationTests {

    @ServiceConnection
    static MySQLContainer mySQLContainer = new MySQLContainer("mysql:8.3.0");
    @LocalServerPort
    private Integer port;

    @BeforeEach
    void setup(){
        RestAssured.baseURI = "http://localhost";
        RestAssured.port = port;
    }

    static {
        mySQLContainer.start();
    }

    @Test
    void shouldReturnTrue() {

        boolean response1 = RestAssured.given()
                .when()
                .get("/api/inventories?skuCode=iphone_15&quantity=100")
                .then()
                .statusCode(200)
                .extract().response().as(Boolean.class);
        assertTrue(response1);


    }

    @Test
    void shouldReturnFalse(){
        boolean response2 = RestAssured.given()
                .when()
                .get("/api/inventories?skuCode=iphone_15&quantity=101")
                .then()
                .statusCode(200)
                .extract().response().as(Boolean.class);
        assertFalse(response2);
    }

}
