package com.nic.nic_shop_task.integration;

import com.nic.nic_shop_task.dtos.categories.UpdateCategoryDto;
import com.nic.nic_shop_task.models.Category;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class CategoryIntegrationTest extends IntegrationTestBase {

    @Autowired
    protected TestRestTemplate restTemplate;
    private Long createdCategoryId;

    @Test
    @Order(1)
    void checkDependentsOn() {
        // For patch and put requests
        restTemplate.getRestTemplate().setRequestFactory(new HttpComponentsClientHttpRequestFactory());
        checkDependencies("CategoryIntegrationTest", "AuthIntegrationTest");
    }

    @Test
    @Order(2)
    void createCategoryTest() {
        Map<String, Object> category = new HashMap<>();
        category.put("name", "Test Category");

        HttpEntity<Map<String, Object>> requestEntity = new HttpEntity<>(category, httpHeaders);

        ResponseEntity<Category> response = restTemplate.postForEntity(
                "/categories/admin",
                requestEntity,
                Category.class
        );

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertTrue(response.hasBody());
        assertEquals("Test Category", response.getBody().getName());
        assertNotNull(response.getBody().getId());
        createdCategoryId = response.getBody().getId();
    }

    @Test
    @Order(3)
    void getCategoryTreeTest() {
        ResponseEntity<Object> response = restTemplate.getForEntity("/categories", Object.class);

        assertEquals(HttpStatus.OK, response.getStatusCode());

        Object responseBody = response.getBody();
        assertNotNull(responseBody);
        assertInstanceOf(List.class, responseBody, "Response body is not a list");

        List<?> categories = (List<?>) responseBody;
        assertFalse(categories.isEmpty(), "Categories list is empty");
        assertInstanceOf(Map.class, categories.get(0), "First element is not a Map");
    }

    @Test
    @Order(4)
    void getDefaultCategoriesTest() {
        ResponseEntity<Object> response = restTemplate.getForEntity(
                "/categories/default",
                Object.class
        );

        assertEquals(HttpStatus.OK, response.getStatusCode());

        Object responseBody = response.getBody();
        assertNotNull(responseBody);
        assertInstanceOf(List.class, responseBody, "Response body is not a list");

        List<?> categories = (List<?>) responseBody;
        assertFalse(categories.isEmpty(), "Categories list is empty");
        assertInstanceOf(Map.class, categories.get(0), "First element is not a Map");
    }

    @Test
    @Order(5)
    void updateCategoryTest() {
        UpdateCategoryDto updateCategoryDto = new UpdateCategoryDto();
        updateCategoryDto.setId(createdCategoryId);
        updateCategoryDto.setName("Test Category after update");

        HttpEntity<UpdateCategoryDto> requestEntity = new HttpEntity<>(updateCategoryDto, httpHeaders);

        ResponseEntity<Object> response = restTemplate.exchange(
                "/categories/admin",
                HttpMethod.PATCH,
                requestEntity,
                Object.class
        );

        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    @Order(6)
    void updateParentCategoryTest() {
        HttpEntity<Void> requestEntity = new HttpEntity<>(httpHeaders);

        ResponseEntity<Object> response = restTemplate.exchange(
                "/categories/admin/parent?categoryId=5&newParentId=2",
                HttpMethod.PATCH,
                requestEntity,
                Object.class
        );

        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    @Order(7)
    void deleteCategoryTest() {
        HttpEntity<Void> requestEntity = new HttpEntity<>(httpHeaders);

        ResponseEntity<?> response = restTemplate.exchange(
                "/categories/admin?id=" + createdCategoryId,
                HttpMethod.DELETE,
                requestEntity,
                Void.class
        );

        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    @Order(100)
    void onEnd() {
        dependencyStatus.put("CategoryIntegrationTest", true);
    }
}
