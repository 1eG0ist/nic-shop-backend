package com.nic.nic_shop_task.integration.util;

import org.testcontainers.containers.PostgreSQLContainer;

public class PostgresTestContainer {
    private static final PostgreSQLContainer<?> POSTGRES_CONTAINER;

    static {
        POSTGRES_CONTAINER = new PostgreSQLContainer<>("postgres:14")
                .withDatabaseName("testdb")
                .withUsername("test")
                .withPassword("test");
        POSTGRES_CONTAINER.start();
        System.out.println("TEST DB STARTED: " + POSTGRES_CONTAINER.getJdbcUrl());
    }

    public static PostgreSQLContainer<?> getInstance() {
        return POSTGRES_CONTAINER;
    }
}
