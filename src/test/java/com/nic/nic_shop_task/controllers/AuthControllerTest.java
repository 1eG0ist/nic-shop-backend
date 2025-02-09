//package com.nic.nic_shop_task.controllers;
//
//import com.nic.nic_shop_task.dtos.AuthRequestDto;
//import com.nic.nic_shop_task.dtos.RegistrationUserDto;
//import com.nic.nic_shop_task.models.User;
//import com.nic.nic_shop_task.services.AuthService;
//import org.junit.jupiter.api.DisplayName;
//import org.junit.jupiter.api.Test;
//import org.junit.jupiter.api.extension.ExtendWith;
//import org.mockito.InjectMocks;
//import org.mockito.Mock;
//import org.mockito.junit.jupiter.MockitoExtension;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.http.HttpStatus;
//import org.springframework.http.ResponseEntity;
//
//import java.util.HashMap;
//import java.util.Map;
//
//import static org.junit.jupiter.api.Assertions.*;
//import static org.mockito.ArgumentMatchers.any;
//import static org.mockito.Mockito.when;
//
//@ExtendWith(MockitoExtension.class)
//@DisplayName("Модульные тесты AuthController")
//@SpringBootTest
//public class AuthControllerTest {
//    @Mock
//    AuthService authService;
//
//    @InjectMocks
//    AuthController authController;
//
//    @Test
//    @DisplayName("Create user - Создаст нового пользователя и отправит jwt токены")
//    void createUser() {
//        RegistrationUserDto registrationUserDto = new RegistrationUserDto();
//        registrationUserDto.setName("Seva");
//        registrationUserDto.setSurname("Kozlovv");
//        registrationUserDto.setEmail("diadypetya@yandex.ru");
//        registrationUserDto.setPassword("123123123");
//
//        // Мокируем вызов сервиса
//        when(authService.createNewUser(any(RegistrationUserDto.class)))
//                .thenAnswer(invocation -> {
//                    Map<String, Object> responseMap = new HashMap<>();
//                    responseMap.put("accessToken", "dummy.access.token"); // Пример JWT токена
//                    responseMap.put("refreshToken", "dummy-refresh-token"); // Пример UUID
//                    responseMap.put("userId", 1L); // Пример ID
//                    responseMap.put("user", new User()); // Пример пользователя
//                    return ResponseEntity.ok(responseMap);
//                });
//
//        // Вызов метода контроллера
//        ResponseEntity<?> response = authController.createNewUser(registrationUserDto);
//
//        // Проверки
//        assertEquals(HttpStatus.OK, response.getStatusCode());
//
//        Map<String, Object> responseBody = (Map<String, Object>) response.getBody();
//        assertNotNull(responseBody);
//    }
//
//    @Test
//    @DisplayName("Login user - Авторизует пользователя и отправит jwt токены")
//    void loginUser() {
//        AuthRequestDto authRequestDto = new AuthRequestDto();
//        authRequestDto.setEmail("diadypetya@yandex.ru");
//        authRequestDto.setPassword("123123123");
//
//        // Мокируем вызов сервиса
//        when(authService.authTokensForLogin(any(AuthRequestDto.class)))
//                .thenAnswer(invocation -> {
//                    Map<String, Object> responseMap = new HashMap<>();
//                    responseMap.put("accessToken", "dummy.access.token"); // Пример JWT токена
//                    responseMap.put("refreshToken", "dummy-refresh-token"); // Пример UUID
//                    responseMap.put("userId", 1L); // Пример ID
//                    responseMap.put("user", new User()); // Пример пользователя
//                    return ResponseEntity.ok(responseMap);
//                });
//
//        // Вызов метода контроллера
//        ResponseEntity<?> response = authController.authTokensForLogin(authRequestDto);
//
//        // Проверки
//        assertEquals(HttpStatus.OK, response.getStatusCode());
//
//        Map<String, Object> responseBody = (Map<String, Object>) response.getBody();
//        assertNotNull(responseBody);
//    }
//}
