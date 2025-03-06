package com.nic.nic_shop_task.integration;

import com.nic.nic_shop_task.models.Product;
import com.nic.nic_shop_task.models.User;
import com.nic.nic_shop_task.repositories.UserRepository;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;


@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@Order(2)
public class ProductIntegrationTest extends IntegrationTestBase {

    @Autowired
    protected TestRestTemplate restTemplate;

    @Autowired
    private UserRepository userRepository;

    @Test
    @Order(1)
    void getProductById_ShouldReturnProduct() {
        Optional<User> user = userRepository.findUserById(1L);

        System.out.println("~~~~1: " + user.isPresent());
        if (user.isPresent()) System.out.println("~~~~1: " + user.get().getName());
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
        System.out.println(product.getName());
        System.out.println("~~~: " + adminJwtToken);
    }
}