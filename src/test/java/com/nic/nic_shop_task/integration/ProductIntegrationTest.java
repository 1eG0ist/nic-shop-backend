package com.nic.nic_shop_task.integration;

import com.nic.nic_shop_task.models.Product;
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
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;


@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class ProductIntegrationTest extends IntegrationTestBase {

    @Autowired
    protected TestRestTemplate restTemplate;
    private Product createdProduct;

    @Test
    @Order(1)
    void checkDependentsOn() {
        // For patch and put requests
        restTemplate.getRestTemplate().setRequestFactory(new HttpComponentsClientHttpRequestFactory());
        checkDependencies("ProductIntegrationTest", "AuthIntegrationTest");
    }

    @Test
    @Order(2)
    void getProductByIdTest() {
        Long productId = 1L;

        ResponseEntity<Product> response = restTemplate.getForEntity(
                "/products?id={id}",
                Product.class,
                productId
        );

        assertEquals(HttpStatus.OK, response.getStatusCode());

        Product product = response.getBody();
        assertNotNull(product);

        assertEquals(productId, product.getId());
    }

    @Test
    @Order(3)
    void getProductsFilteredProductsByCategoryTest() {
        Map<String, Object> prop = new HashMap<>();
        prop.put("propertyId", 3);
        prop.put("textValue", "Золотой");
        List<Map<String, Object>> props = Collections.singletonList(prop);
        HttpEntity<List<Map<String, Object>>> requestEntity = new HttpEntity<>(props, httpHeaders);

        ResponseEntity<Map<String, Object>> response = restTemplate.exchange(
                "/products/by_category?id=1&sort=asc&page=0",
                HttpMethod.POST,
                requestEntity,
                new ParameterizedTypeReference<Map<String, Object>>() {}
        );

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());

        List<Object> content = (List<Object>) response.getBody().get("content");
        assertNotNull(content);
        assertInstanceOf(List.class, content, "'content' не является списком");

        assertEquals(1, content.size(), "'content' должно содержать хотя бы 1 продукт");

        Object firstElement = content.get(0);
        assertInstanceOf(Map.class, firstElement, "Первый элемент 'content' не является Map");

        Map<String, Object> productMap = (Map<String, Object>) firstElement;
        assertEquals(4, productMap.get("id"), "ID продукта не совпадает");
        assertEquals("Смартфон Huawei P50", productMap.get("name"), "Название продукта не совпадает");
    }

    @Test
    @Order(4)
    void createProductTest() {
        Product product = new Product();
        product.setName("Тестовый продукт");
        product.setDescription("Описание тестового продукта");
        product.setPrice(9999.99);
        product.setCount(10);
        product.setImagePath("/images/no_product_photo.png");
        product.setSale(15);

        HttpEntity<Product> requestEntity = new HttpEntity<>(product, httpHeaders);

        ResponseEntity<Product> response = restTemplate.postForEntity(
                "/products/admin",
                requestEntity,
                Product.class
        );

        assertEquals(HttpStatus.CREATED, response.getStatusCode());

        assertNotNull(response.getBody());

        Product createdProductCheck = response.getBody();
        assertNotNull(createdProductCheck.getId(), "ID созданного продукта не должен быть null");
        assertEquals(product.getName(), createdProductCheck.getName(), "Название продукта не совпадает");
        assertEquals(product.getDescription(), createdProductCheck.getDescription(), "Описание продукта не совпадает");
        assertEquals(product.getPrice(), createdProductCheck.getPrice(), "Цена продукта не совпадает");
        assertEquals(product.getCount(), createdProductCheck.getCount(), "Количество товара не совпадает");
        assertEquals(product.getImagePath(), createdProductCheck.getImagePath(), "Путь к изображению не совпадает");
        assertEquals(product.getSale(), createdProductCheck.getSale(), "Скидка не совпадает");

        createdProduct = createdProductCheck;
    }


    @Test
    @Order(5)
    void updateProductTest() {
        createdProduct.setCount(22);

        HttpEntity<Product> requestEntity = new HttpEntity<>(createdProduct, httpHeaders);

        ResponseEntity<Product> response = restTemplate.exchange(
                "/products/admin",
                HttpMethod.PATCH,
                requestEntity,
                Product.class
        );

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());

        Product updatedResponseProduct = response.getBody();
        assertEquals(22, updatedResponseProduct.getCount(), "Количество товара должно быть обновлено до 22");
    }

    @Test
    @Order(6)
    void checkProductCountTest() {
        ResponseEntity<Integer> response = restTemplate.getForEntity(
                "/products/check?id=" + createdProduct.getId(),
                Integer.class
        );

        assertEquals(HttpStatus.OK, response.getStatusCode());
        Integer count = response.getBody();
        assertNotNull(count);
        assertEquals(count, 22, "Количество не совпадает, после обновления ожидается 22, вернулось " + count);
    }

    @Test
    @Order(7)
    void deleteProductTest() {
        HttpEntity<Void> requestEntity = new HttpEntity<>(httpHeaders);

        ResponseEntity<?> response = restTemplate.exchange(
                "/products/admin?id=" + createdProduct.getId(),
                HttpMethod.DELETE,
                requestEntity,
                Void.class
        );

        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    @Order(100)
    void onEnd() {
        dependencyStatus.put("ProductIntegrationTest", true);
    }
}