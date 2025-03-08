package com.nic.nic_shop_task.integration;

import com.nic.nic_shop_task.dtos.CellWithOutOrderDto;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class OrderIntegrationTest extends IntegrationTestBase {
    @Autowired
    protected TestRestTemplate restTemplate;

    @Test
    @Order(1)
    void checkDependentsOn() {
        restTemplate.getRestTemplate().setRequestFactory(new HttpComponentsClientHttpRequestFactory());
        checkDependencies("OrderIntegrationTest", "AuthIntegrationTest");
    }

    @Test
    @Order(2)
    void createOrderTest() {
        List<CellWithOutOrderDto> cells = Arrays.asList(
                new CellWithOutOrderDto(1L, 2),
                new CellWithOutOrderDto(2L, 3),
                new CellWithOutOrderDto(6L, 6)
        );

        HttpEntity<List<CellWithOutOrderDto>> requestEntity = new HttpEntity<>(cells, httpHeaders);

        ResponseEntity<com.nic.nic_shop_task.models.Order> response = restTemplate.postForEntity(
                "/orders",
                requestEntity,
                com.nic.nic_shop_task.models.Order.class
        );

        assertEquals(HttpStatus.OK, response.getStatusCode());

    }
}
