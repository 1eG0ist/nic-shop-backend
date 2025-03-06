package com.nic.nic_shop_task.integration;

import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class CategoryIntegrationTest {


    @Test
    @Order(1)
    void getCategoryTreeTest() {
        System.out.println("HERE IS CATEGORY TREE TEST");
    }
}
