package com.nic.nic_shop_task.services;

import com.nic.nic_shop_task.dtos.AuthRequestDto;
import com.nic.nic_shop_task.dtos.RegistrationUserDto;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;

public interface AuthService {
    ResponseEntity<?> authTokensForLogin(@RequestBody AuthRequestDto authRequest);
    ResponseEntity<?> createNewUser(@RequestBody RegistrationUserDto registrationUserDto);
    ResponseEntity<?> createTokensByRefreshToken(String token);
}
