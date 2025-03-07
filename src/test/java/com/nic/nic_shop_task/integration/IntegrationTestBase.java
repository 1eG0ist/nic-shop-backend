package com.nic.nic_shop_task.integration;

import com.nic.nic_shop_task.integration.util.PostgresTestContainer;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.ClassOrderer;
import org.junit.jupiter.api.TestClassOrder;
import org.junit.jupiter.api.TestInstance;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.PostgreSQLContainer;

import java.util.HashMap;
import java.util.Map;

@ActiveProfiles("test")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@TestClassOrder(ClassOrderer.OrderAnnotation.class)
public abstract class IntegrationTestBase {

    protected static String adminJwtToken;
    protected static HttpHeaders httpHeaders;

    /*
    * TestNg не ожидает запуска docker postgres контейнера, JUnit меньше подходит для интеграционных тестов, т.к. нет
    * порядка выполнения тестовых классов, поэтому порядок задан таким образом
    * */
    protected static final Map<String, Boolean> dependencyStatus = new HashMap<String, Boolean>() {{
        put("AuthIntegrationTest", false);
        put("CategoryIntegrationTest", false);
        put("ProductIntegrationTest", false);
    }};

    protected static void checkDependencies(String currentClassName, String dependencyClassName) {
        while (!dependencyStatus.getOrDefault(dependencyClassName, false)) {
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        System.out.println(currentClassName + " can start now because " + dependencyClassName + " has finished.");
    }

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
