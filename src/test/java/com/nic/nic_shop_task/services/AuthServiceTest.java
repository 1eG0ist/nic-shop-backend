package com.nic.nic_shop_task.services;

import com.nic.nic_shop_task.configs.MyUserDetails;
import com.nic.nic_shop_task.dtos.AuthRequestDto;
import com.nic.nic_shop_task.dtos.RegistrationUserDto;
import com.nic.nic_shop_task.models.RefreshToken;
import com.nic.nic_shop_task.models.User;
import com.nic.nic_shop_task.repositories.UserSensitiveDataRepository;
import com.nic.nic_shop_task.services.Impl.AccessTokenServiceImpl;
import com.nic.nic_shop_task.services.Impl.AuthServiceImpl;
import com.nic.nic_shop_task.services.Impl.RefreshTokenServiceImpl;
import com.nic.nic_shop_task.services.Impl.UserServiceImpl;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;

import java.util.Map;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("Модульные тесты AuthService")
public class AuthServiceTest {

    @Mock
    private UserServiceImpl userService;

    @Mock
    private AccessTokenServiceImpl accessTokenService;

    @Mock
    private UserSensitiveDataService userSensitiveDataService;

    @Mock
    private UserSensitiveDataRepository userSensitiveDataRepository;

    @Mock
    private RefreshTokenServiceImpl refreshTokenService;

    @Mock
    private AuthenticationManager authenticationManager;

    @InjectMocks
    private AuthServiceImpl authService;

    @Test
    @DisplayName("Login user - Авторизует пользователя и отправит jwt токены")
    void authTokensForLogin() {
        AuthRequestDto authRequestDto = new AuthRequestDto();
        authRequestDto.setEmail("diadypetya@yandex.ru");
        authRequestDto.setPassword("123123123");

        User user = new User();
        user.setId(1L);
        user.setEmail("diadypetya@yandex.ru");

        when(userService.existsByEmail(authRequestDto.getEmail())).thenReturn(Optional.of(user));
        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class))).thenReturn(null);

        when(accessTokenService.generateToken(any())).thenCallRealMethod();
        when(refreshTokenService.createRefreshToken(anyString())).thenCallRealMethod();

        ResponseEntity<?> response = authService.authTokensForLogin(authRequestDto);

        assertEquals(HttpStatus.OK, response.getStatusCode());

        Map<String, Object> responseBody = (Map<String, Object>) response.getBody();
        assertNotNull(responseBody);

        String accessToken = (String) responseBody.get("accessToken");
        assertNotNull(accessToken);
        assertTrue(accessToken.startsWith("Bearer "), "accessToken должен начинаться с 'Bearer '");

        String refreshToken = (String) responseBody.get("refreshToken");
        assertNotNull(refreshToken);
        assertTrue(refreshToken.contains("-"), "refreshToken должен содержать хотя бы одно тире");

        Long userId = (Long) responseBody.get("userId");
        assertNotNull(userId);
        assertTrue(userId > 0, "userId должен быть положительным числом");

        User returnedUser = (User) responseBody.get("user");
        assertNotNull(returnedUser);
    }

    @Test
    @DisplayName("Create tokens by refresh token - Создаст токены по refresh токену")
    void createTokensByRefreshToken() {
        String token = "dummy-refresh-token";
        User user = new User();
        user.setId(1L);
        user.setEmail("diadypetya@yandex.ru");

        RefreshToken refreshToken = new RefreshToken();
        refreshToken.setToken(token);
        refreshToken.setUser(user);

        MyUserDetails userDetails = new MyUserDetails(user, userSensitiveDataRepository);

        when(refreshTokenService.findByToken(token)).thenReturn(Optional.of(refreshToken));
        when(refreshTokenService.verifyExpiration(any(RefreshToken.class))).thenReturn(Optional.of(refreshToken));
        when(userService.findByEmail(user.getEmail())).thenReturn(userDetails);
        when(accessTokenService.generateToken(any())).thenCallRealMethod();

        ResponseEntity<?> response = authService.createTokensByRefreshToken(token);

        assertEquals(HttpStatus.OK, response.getStatusCode());

        Map<String, Object> responseBody = (Map<String, Object>) response.getBody();
        assertNotNull(responseBody);

        String accessToken = (String) responseBody.get("accessToken");
        assertNotNull(accessToken);
        assertTrue(accessToken.startsWith("Bearer "), "accessToken должен начинаться с 'Bearer '");

        String returnedRefreshToken = (String) responseBody.get("refreshToken");
        assertNotNull(returnedRefreshToken);
        assertEquals(token, returnedRefreshToken, "refreshToken должен быть равен переданному токену");

        User returnedUser = (User) responseBody.get("user");
        assertNotNull(returnedUser);
    }

    @Test
    @DisplayName("Create new user - Создаст нового пользователя и отправит jwt токены")
    void createNewUser() {
        // TODO understand how test real work of service with out saving test users to real db
        RegistrationUserDto registrationUserDto = new RegistrationUserDto();
        registrationUserDto.setName("Seva");
        registrationUserDto.setSurname("Kozlovv");
        registrationUserDto.setEmail("diadypetya@yandex.ru");
        registrationUserDto.setPassword("123123123");

        User user = new User();
        user.setId(1L);
        user.setEmail("diadypetya@yandex.ru");

        when(userService.findByEmail(registrationUserDto.getEmail())).thenReturn(null);
        when(userService.createNewUser(any(RegistrationUserDto.class))).thenReturn(user);
        doNothing().when(userSensitiveDataService).savePassword(anyLong(), anyString());
        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class))).thenReturn(null);
        when(accessTokenService.generateToken(any())).thenCallRealMethod();
        when(refreshTokenService.createRefreshToken(anyString())).thenCallRealMethod();

        ResponseEntity<?> response = authService.createNewUser(registrationUserDto);

        assertEquals(HttpStatus.OK, response.getStatusCode());

        Map<String, Object> responseBody = (Map<String, Object>) response.getBody();
        assertNotNull(responseBody);

        String accessToken = (String) responseBody.get("accessToken");
        assertNotNull(accessToken);
        assertTrue(accessToken.startsWith("Bearer "), "accessToken должен начинаться с 'Bearer '");

        String refreshToken = (String) responseBody.get("refreshToken");
        assertNotNull(refreshToken);
        assertTrue(refreshToken.contains("-"), "refreshToken должен содержать хотя бы одно тире");

        Long userId = (Long) responseBody.get("userId");
        assertNotNull(userId);
        assertTrue(userId > 0, "userId должен быть положительным числом");

        User returnedUser = (User) responseBody.get("user");
        assertNotNull(returnedUser);
    }
}
