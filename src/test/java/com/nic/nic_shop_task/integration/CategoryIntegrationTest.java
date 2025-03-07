package com.nic.nic_shop_task.integration;

import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class CategoryIntegrationTest extends IntegrationTestBase {

    @Autowired
    protected TestRestTemplate restTemplate;

    @Test
    @Order(1)
    void checkDependentsOn() {
        checkDependencies("CategoryIntegrationTest", "AuthIntegrationTest");
    }

    @Test
    @Order(2)
    void createCategoryTest() {
        Map<String, Object> category = new HashMap<>();
        category.put("name", "Test Category");

        HttpEntity<Map<String, Object>> requestEntity = new HttpEntity<>(category, httpHeaders);
        ResponseEntity<String> response = restTemplate.postForEntity(
                "/categories/admin",
                requestEntity,
                String.class
        );

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
    }

    @Test
    @Order(3)
    void getCategoryTreeTes() {

    }

    @Test
    @Order(100)
    void onEnd() {
        dependencyStatus.put("CategoryIntegrationTest", true);
    }
}
