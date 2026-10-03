package com.shopping.product;

import com.shopping.product.model.Product;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.context.annotation.Import;
import org.testcontainers.containers.MongoDBContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.math.BigDecimal;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;

@Testcontainers
@Import(TestcontainersConfiguration.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class ProductServiceApplicationTests {

	@Container
	@ServiceConnection
	static MongoDBContainer mongoDBContainer = new MongoDBContainer("mongo:7.0.5");

	@LocalServerPort
	private Integer port;

	// 1. Tell RestAssured to use the random port Spring Boot generated
	@BeforeEach
	void setup() {
		RestAssured.baseURI = "http://localhost";
		RestAssured.port = port;
	}

	@Test
	void shouldCreateProduct() {
		Product request = Product.builder()
				.name("iPhone 15")
				.description("Apple iPhone 15 128GB Black")
				.price(new BigDecimal("79999.99"))
				.build();

		// 2. Use RestAssured's Given/When/Then syntax
		RestAssured.given()
				.contentType(ContentType.JSON)
				.body(request)
				.when()
				.post("/api/product")
				.then()
				.statusCode(201) // Adjust to 200 if your controller returns 200 OK
				.body("id", notNullValue())
				.body("name", equalTo("iPhone 15"))
				.body("description", equalTo("Apple iPhone 15 128GB Black"))
				// Note: Depending on how Jackson serializes BigDecimals, you might need to test this as a float/double
				.body("price", equalTo(79999.99f));
	}
}