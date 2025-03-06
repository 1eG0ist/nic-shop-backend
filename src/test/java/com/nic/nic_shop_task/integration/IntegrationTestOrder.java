//package com.nic.nic_shop_task.integration;
//
//import org.junit.jupiter.api.ClassOrderer;
//import org.junit.jupiter.api.Order;
//import org.junit.jupiter.api.TestClassOrder;
//
//@TestClassOrder(ClassOrderer.OrderAnnotation.class) // Указываем, что порядок будет задаваться аннотациями @Order
//public class IntegrationTestOrder {
//
//    @Order(1) // Указываем порядок для AuthIntegrationTest
//    public static class AuthTest extends AuthIntegrationTest {
//    }
//
//    @Order(2) // Указываем порядок для ProductIntegrationTest
//    public static class ProductTest extends ProductIntegrationTest {
//    }
//}
