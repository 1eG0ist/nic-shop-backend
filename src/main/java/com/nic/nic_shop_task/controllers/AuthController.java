package com.nic.nic_shop_task.controllers;

import com.nic.nic_shop_task.dtos.*;
import com.nic.nic_shop_task.services.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.NoSuchElementException;

@RestController()
@RequiredArgsConstructor
@RequestMapping("/auth")
public class AuthController {
    private final AuthService authService;

    @PostMapping("/login")
    public ResponseEntity<?> authTokensForLogin(@RequestBody AuthRequestDto authRequest) {
        try {
            AuthResponseDto authResponseDto = authService.authTokensForLogin(authRequest);
            return ResponseEntity.ok(authResponseDto);
        } catch (NoSuchElementException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (AuthenticationException e) {
            return ResponseEntity.badRequest().body("Wrong login or password");
        }
    }

    @PostMapping("/refresh-token")
    public ResponseEntity<?> createTokensByRefreshToken(@RequestBody RefreshTokenRequestDto refreshTokenRequestDTO){
        try {
            JwtResponseDto jwtResponseDto = authService.createTokensByRefreshToken(refreshTokenRequestDTO.getRefreshToken());
            return ResponseEntity.ok().body(jwtResponseDto);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Something went wrong by creating tokens");
        }
    }

    @PostMapping("/registration")
    public ResponseEntity<?> createNewUser(@RequestBody RegistrationUserDto registrationUserDto) {
        try {
            AuthResponseDto authResponseDto = authService.createNewUser(registrationUserDto);
            return ResponseEntity.ok().body(authResponseDto);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
