package com.nic.nic_shop_task.integration;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.TestInstance;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

@ActiveProfiles("test")
@Testcontainers
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public abstract class IntegrationTestBase {

    protected static String adminJwtToken; // Токен для администратора

    @Container
    public static PostgreSQLContainer<?> postgreSQLContainer = new PostgreSQLContainer<>("postgres:14")
            .withDatabaseName("testdb")
            .withUsername("test")
            .withPassword("test");

    @BeforeAll
    static void beforeAll() {
        postgreSQLContainer.start();
        System.out.println("TEST DB STARTED: " + postgreSQLContainer.getJdbcUrl());
    }

//    static {
//        postgreSQLContainer.start();
//        System.out.println("TEST DB STARTED");
////        if (!postgreSQLContainer.isRunning()) {
////            postgreSQLContainer.start();
////            try {
////                Thread.sleep(5000);
////                System.out.println("PostgreSQL container started for tests: " + postgreSQLContainer.getJdbcUrl());
////            } catch (InterruptedException e) {
////                Thread.currentThread().interrupt();
////            }
////        }
//    }

    @DynamicPropertySource
    static void configureTestDatabase(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", postgreSQLContainer::getJdbcUrl);
        registry.add("spring.datasource.username", postgreSQLContainer::getUsername);
        registry.add("spring.datasource.password", postgreSQLContainer::getPassword);
    }
}