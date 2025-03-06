package com.nic.nic_shop_task.integration;

import org.junit.platform.suite.api.SelectClasses;
import org.junit.platform.suite.api.Suite;
import org.junit.platform.suite.api.SuiteDisplayName;

@Suite
@SuiteDisplayName("Integration Test Suite")
@SelectClasses({
        ProductIntegrationTest.class,
        AuthIntegrationTest.class,
})
public class IntegrationTestSuite {
}