package com.nic.nic_shop_task.controllers;

import com.nic.nic_shop_task.dtos.AuthRequestDto;
import com.nic.nic_shop_task.dtos.RefreshTokenRequestDto;
import com.nic.nic_shop_task.dtos.RegistrationUserDto;
import com.nic.nic_shop_task.services.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController()
@RequiredArgsConstructor
@RequestMapping("/auth")
public class AuthController {
    private final AuthService authService;

    @PostMapping("/login")
    public ResponseEntity<?> authTokensForLogin(@RequestBody AuthRequestDto authRequest) {
        return authService.authTokensForLogin(authRequest);
    }

    @PostMapping("/refreshToken")
    public ResponseEntity<?> createTokensByRefreshToken(@RequestBody RefreshTokenRequestDto refreshTokenRequestDTO){
        return authService.createTokensByRefreshToken(refreshTokenRequestDTO.getRefreshToken());
    }

    @PostMapping("/registration")
    public ResponseEntity<?> createNewUser(@RequestBody RegistrationUserDto registrationUserDto) {
        return authService.createNewUser(registrationUserDto);
    }
}
