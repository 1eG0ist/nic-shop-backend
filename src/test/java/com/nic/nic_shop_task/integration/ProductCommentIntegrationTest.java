package com.nic.nic_shop_task.integration;

import com.nic.nic_shop_task.dtos.CreateProductCommentDto;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;


@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class ProductCommentIntegrationTest extends IntegrationTestBase{
    @Autowired
    protected TestRestTemplate restTemplate;
    private Long createdProductCommentId;

    @Test
    @Order(1)
    void checkDependentsOn() {
        checkDependencies("ProductCommentIntegrationTest", "AuthIntegrationTest");
    }

    @Test
    @Order(2)
    void createProductCommentTest() {
        CreateProductCommentDto productComment = new CreateProductCommentDto(
                1L,
                5,
                "Хороший продуктик",
                null
        );
        HttpEntity<CreateProductCommentDto> requestEntity = new HttpEntity<>(productComment, httpHeaders);
        ResponseEntity<Map<String, Object>> response = restTemplate.exchange(
                "/product_comments",
                HttpMethod.POST,
                requestEntity,
                new ParameterizedTypeReference<Map<String, Object>>() {}  // Указываем конкретные типы
        );

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody(), "Ответ от сервера пустой");

        Map<String, Object> responseBody = response.getBody();
        assertTrue(responseBody.containsKey("id"), "Ответ не содержит ID");

        Long createdProductCommentIdCheck = ((Number) responseBody.get("id")).longValue();  // Корректное извлечение ID
        assertNotNull(createdProductCommentIdCheck, "createdProductCommentId не должен быть null");
        createdProductCommentId = createdProductCommentIdCheck;
    }

    @Test
    @Order(3)
    void getProductCommentsByProductIdTest() {
        HttpEntity<Void> requestEntity = new HttpEntity<>(httpHeaders);

        ResponseEntity<Map<String, Object>> response = restTemplate.exchange(
                "/product_comments/all?id=1&page=0",
                HttpMethod.GET,
                requestEntity,
                new ParameterizedTypeReference<Map<String, Object>>() {}
        );

        assertInstanceOf(Map.class, response.getBody(), "В ответе пришел неверный тип данных");

        List<Object> content = (List<Object>) response.getBody().get("content");
        assertNotNull(content);

        assertInstanceOf(List.class, content, "'content' не является списком");

        assertEquals(1, content.size(), "'content' должно содержать хотя бы 1 продукт");

        Object firstElement = content.get(0);
        assertInstanceOf(Map.class, firstElement, "Первый элемент 'content' не является Map");

        Map<String, Object> productMap = (Map<String, Object>) firstElement;
        assertEquals(createdProductCommentId, ((Number) productMap.get("id")).longValue(), "ID комментария не совпадает");
        assertEquals("Хороший продуктик", productMap.get("comment"), "Комментарий не совпадает");
    }

    @Test
    @Order(4)
    void deleteProductCommentTest() {
        HttpEntity<Void> requestEntity = new HttpEntity<>(httpHeaders);

        ResponseEntity<?> response = restTemplate.exchange(
                "/product_comments/admin?id=" + createdProductCommentId,
                HttpMethod.DELETE,
                requestEntity,
                Void.class
        );

        assertEquals(HttpStatus.OK, response.getStatusCode());
    }
}
