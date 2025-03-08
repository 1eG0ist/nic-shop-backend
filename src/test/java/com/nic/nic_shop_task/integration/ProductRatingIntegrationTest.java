package com.nic.nic_shop_task.integration;

import com.nic.nic_shop_task.dtos.ProductRatingDto;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.assertEquals;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class ProductRatingIntegrationTest extends IntegrationTestBase{
    @Autowired
    protected TestRestTemplate restTemplate;

    @Test
    @Order(1)
    void checkDependentsOn() {
        checkDependencies("ProductRatingIntegrationTest", "AuthIntegrationTest");
    }

    @Test
    @Order(2)
    void addProductRatingTest() {
        ProductRatingDto productRatingDto = new ProductRatingDto(1L, 4);
        HttpEntity<ProductRatingDto> httpEntity = new HttpEntity<>(productRatingDto, httpHeaders);
        ResponseEntity<Void> response = restTemplate.postForEntity(
                "/products/rating",
                httpEntity,
                Void.class
        );

        assertEquals(HttpStatus.OK, response.getStatusCode(), "~~~ Wrong status code");
    }
}
