package com.nic.nic_shop_task.integration;

import com.nic.nic_shop_task.dtos.ProductPropertyDto;
import com.nic.nic_shop_task.models.ProductProperties;
import com.nic.nic_shop_task.models.Property;
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

import static org.junit.jupiter.api.Assertions.*;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)

public class ProductPropertiesIntegrationTest extends IntegrationTestBase {
    @Autowired
    protected TestRestTemplate restTemplate;
    private ProductProperties createdProductProperty;

    @Test
    @Order(1)
    void checkDependentsOn() {
        // For patch and put requests
        restTemplate.getRestTemplate().setRequestFactory(new HttpComponentsClientHttpRequestFactory());
        checkDependencies("ProductPropertiesIntegrationTest", "AuthIntegrationTest");
        checkDependencies("ProductPropertiesIntegrationTest", "ProductIntegrationTest");
    }

    @Test
    @Order(2)
    void createProductPropertyTest() {
        ProductPropertyDto productPropertyDto = new ProductPropertyDto();
        Property property = new Property(3L, "Цвет", "Текст");
        productPropertyDto.setProperty(property);
        productPropertyDto.setTextValue("Фиолетовый");
        productPropertyDto.setProductId(1L);

        HttpEntity<ProductPropertyDto> entity = new HttpEntity<>(productPropertyDto, httpHeaders);

        ResponseEntity<ProductProperties> response = restTemplate.postForEntity(
                "/product_properties/admin",
                entity,
                ProductProperties.class);

        assertEquals(HttpStatus.CREATED, response.getStatusCode(), "Возвращен неверный код ответа");
        assertInstanceOf(ProductProperties.class, response.getBody(), "Возвращен неверный тип");
        assertNotNull(response.getBody().getId());
        createdProductProperty = response.getBody();
    }

    @Test
    @Order(3)
    void updateProductPropertyTest() {
        createdProductProperty.setTextValue("Розовый");
        HttpEntity<ProductProperties> requestEntity = new HttpEntity<>(createdProductProperty, httpHeaders);

        ResponseEntity<?> response = restTemplate.exchange(
                "/product_properties/admin",
                HttpMethod.PATCH,
                requestEntity,
                Void.class
        );

        assertEquals(HttpStatus.OK, response.getStatusCode(), "Статус ответа неверный");
    }

    @Test
    @Order(4)
    void deleteProductPropertyTest() {
        HttpEntity<Void> requestEntity = new HttpEntity<>(httpHeaders);

        ResponseEntity<?> response = restTemplate.exchange(
                "/product_properties/admin?id=" + createdProductProperty.getId(),
                HttpMethod.DELETE,
                requestEntity,
                Void.class
        );

        assertEquals(HttpStatus.OK, response.getStatusCode());
    }
}
