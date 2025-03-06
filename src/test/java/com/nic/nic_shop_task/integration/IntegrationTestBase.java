package com.nic.nic_shop_task.integration;

import com.nic.nic_shop_task.integration.util.PostgresTestContainer;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.ClassOrderer;
import org.junit.jupiter.api.TestClassOrder;
import org.junit.jupiter.api.TestInstance;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.PostgreSQLContainer;

@ActiveProfiles("test")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@TestClassOrder(ClassOrderer.OrderAnnotation.class)
public abstract class IntegrationTestBase {

    protected static String adminJwtToken;

    @BeforeAll
    static void beforeAll() {
        System.out.println("Using shared PostgreSQL container: " + PostgresTestContainer.getInstance().getJdbcUrl());
    }

    @DynamicPropertySource
    static void configureTestDatabase(DynamicPropertyRegistry registry) {
        PostgreSQLContainer<?> container = PostgresTestContainer.getInstance();
        registry.add("spring.datasource.url", container::getJdbcUrl);
        registry.add("spring.datasource.username", container::getUsername);
        registry.add("spring.datasource.password", container::getPassword);
    }
}
