package com.nic.nic_shop_task.integration;

import com.nic.nic_shop_task.models.Product;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
public class ProductIntegrationTest extends IntegrationTestBase {

    @Autowired
    protected TestRestTemplate restTemplate;

    @Test
    @Order(1)
    void getProductById_ShouldReturnProduct() {
        Long productId = 1L;

        // Выполняем GET-запрос к эндпоинту /products?id=1
        ResponseEntity<Product> response = restTemplate.getForEntity(
                "/products?id={id}",
                Product.class,
                productId
        );

        // Проверяем, что статус ответа 200 OK
        assertEquals(HttpStatus.OK, response.getStatusCode());

        // Проверяем, что тело ответа не пустое
        Product product = response.getBody();
        assertNotNull(product);

        // Проверяем, что ID продукта соответствует запрошенному
        assertEquals(productId, product.getId());

        System.out.println("~~~: " + adminJwtToken);
    }
}