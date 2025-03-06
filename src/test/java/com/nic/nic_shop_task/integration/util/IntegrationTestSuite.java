package com.nic.nic_shop_task.integration.util;

import com.nic.nic_shop_task.integration.AuthIntegrationTest;
import com.nic.nic_shop_task.integration.ProductIntegrationTest;
import org.junit.platform.suite.api.SelectClasses;
import org.junit.platform.suite.api.Suite;
import org.junit.platform.suite.api.SuiteDisplayName;

@Suite
@SelectClasses({AuthIntegrationTest.class, ProductIntegrationTest.class})  // Укажите ваши тесты
@SuiteDisplayName("Integration Test Suite")  // Название сьюта для отображения
public class IntegrationTestSuite {
}