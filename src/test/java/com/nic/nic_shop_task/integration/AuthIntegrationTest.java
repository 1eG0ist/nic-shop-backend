package com.nic.nic_shop_task.integration;

import com.nic.nic_shop_task.dtos.AuthRequestDto;
import com.nic.nic_shop_task.dtos.AuthResponseDto;
import com.nic.nic_shop_task.dtos.RegistrationUserDto;
import com.nic.nic_shop_task.repositories.UserRepository;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class AuthIntegrationTest extends IntegrationTestBase {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    protected TestRestTemplate restTemplate;

    @Test
    @Order(1)
    void registerAdminUser() {
        // Шаг 1: Создаём пользователя с ролью администратора
        RegistrationUserDto registrationUserDto = new RegistrationUserDto();
        registrationUserDto.setName("Admin");
        registrationUserDto.setSurname("Adminov");
        registrationUserDto.setEmail("admin@example.com");
        registrationUserDto.setPassword("admin");

        // Выполняем запрос на регистрацию
        ResponseEntity<AuthResponseDto> response = restTemplate.postForEntity(
                "/auth/registration",
                registrationUserDto,
                AuthResponseDto.class
        );

        // Проверяем, что регистрация прошла успешно
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());

        userRepository.addAdminRoleToUser(response.getBody().getUserId());
    }

    @Test
    @Order(2)
    void loginAdminUserAndSaveToken() {
        AuthRequestDto loginRequestDto = new AuthRequestDto();
        loginRequestDto.setEmail("admin@example.com");
        loginRequestDto.setPassword("admin");

        ResponseEntity<AuthResponseDto> response = restTemplate.postForEntity(
                "/auth/login",
                loginRequestDto,
                AuthResponseDto.class
        );

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());

        adminJwtToken = response.getBody().getAccessToken();
    }
}