package com.nic.nic_shop_task.services;

import com.nic.nic_shop_task.dtos.AuthRequestDto;
import com.nic.nic_shop_task.dtos.AuthResponseDto;
import com.nic.nic_shop_task.dtos.JwtResponseDto;
import com.nic.nic_shop_task.dtos.RegistrationUserDto;
import org.springframework.web.bind.annotation.RequestBody;

public interface AuthService {
    AuthResponseDto authTokensForLogin(@RequestBody AuthRequestDto authRequest);
    AuthResponseDto createNewUser(@RequestBody RegistrationUserDto registrationUserDto) throws Exception;
    JwtResponseDto createTokensByRefreshToken(String token);
}
