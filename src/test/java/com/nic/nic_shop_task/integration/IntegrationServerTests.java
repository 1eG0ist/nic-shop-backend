//package com.nic.nic_shop_task.integration;
//
//import org.junit.jupiter.api.Order;
//import org.junit.jupiter.api.Test;
//
//public class IntegrationServerTests extends IntegrationTestBase {
//    private final AuthIntegrationTest authIntegrationTest = new AuthIntegrationTest();
//    private final ProductIntegrationTest productIntegrationTest = new ProductIntegrationTest();
//
//    @Test
//    @Order(1)
//    void testInitialization() { System.out.println("~~~ Tests running correctly"); }
//
//    /*
//    * Auth tests
//    */
//    @Test
//    @Order(10)
//    void registerAdminUserTest() { authIntegrationTest.registerAdminUser(); }
//
//    @Test
//    @Order(15) // Этот тест будет выполняться первым
//    void loginAdminUserAndSaveTokenTest() { authIntegrationTest.loginAdminUserAndSaveToken(); }
//
//    /*
//    * Product tests
//    * */
//    @Test
//    @Order(20)
//    void asd() { productIntegrationTest.getProductById_ShouldReturnProduct(); }
//}
