package com.nic.nic_shop_task.services.Impl;

import com.nic.nic_shop_task.configs.MyUserDetails;
import com.nic.nic_shop_task.dtos.AuthRequestDto;
import com.nic.nic_shop_task.dtos.AuthResponseDto;
import com.nic.nic_shop_task.dtos.JwtResponseDto;
import com.nic.nic_shop_task.dtos.RegistrationUserDto;
import com.nic.nic_shop_task.models.RefreshToken;
import com.nic.nic_shop_task.models.User;
import com.nic.nic_shop_task.services.*;
import lombok.AllArgsConstructor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

import javax.transaction.Transactional;
import java.util.Optional;


@Service
@AllArgsConstructor
public class AuthServiceImpl implements AuthService {
    private final UserService userService;
    private final AccessTokenService accessTokenService;
    private final UserSensitiveDataService userSensitiveDataService;
    private final RefreshTokenService refreshTokenService;
    private final AuthenticationManager authenticationManager;

    public ResponseEntity<?> authTokensForLogin(@RequestBody AuthRequestDto authRequest) {
        Optional<User> user = userService.existsByEmail(authRequest.getEmail());
        if (!user.isPresent()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("User does not exist");
        }

        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(authRequest.getEmail(), authRequest.getPassword())
            );
            AuthResponseDto response = getTokens(authRequest.getEmail());
            response.setUser(user.get());

            return ResponseEntity.ok(response);
        } catch (AuthenticationException ex) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Wrong login or password");
        }
    }


    public ResponseEntity<?> createTokensByRefreshToken(String token){
        return refreshTokenService.findByToken(token)
                .flatMap(refreshTokenService::verifyExpiration)
                .map(RefreshToken::getUser)
                .map(userInfo -> {
                    String accessToken = accessTokenService.generateToken(userService.findByEmail(userInfo.getEmail()));
                    return ResponseEntity.ok(JwtResponseDto.builder()
                            .accessToken(accessToken)
                            .refreshToken(token)
                            .user(userService.existsByEmail(userInfo.getEmail()).get()).build());
                }).orElse(ResponseEntity.badRequest().build());
    }

    public AuthResponseDto getTokens(String email) {
        MyUserDetails userDetails = userService.findByEmail(email);
        Optional<RefreshToken> refreshTokenCheck = refreshTokenService.findByUserId(userDetails.getId());
        if (refreshTokenCheck.isPresent()) {
            return refreshTokenService.findByToken(refreshTokenCheck.get().getToken())
                    .flatMap(refreshTokenService::verifyExpiration)
                    .map(RefreshToken::getUser)
                    .map(userInfo -> {
                        String accessToken = accessTokenService.generateToken(userService.findByEmail(userInfo.getEmail()));
                        AuthResponseDto response = new AuthResponseDto();
                        response.setAccessToken(accessToken);
                        response.setRefreshToken(refreshTokenCheck.get().getToken());
                        response.setUserId(userDetails.getId());
                        return response;
                    }).orElse(new AuthResponseDto());
        }
        // TODO check if refresh token exist return them
        String accessToken = accessTokenService.generateToken(userDetails);
        RefreshToken refreshToken = refreshTokenService.createRefreshToken(email);
        AuthResponseDto response = new AuthResponseDto();
        response.setAccessToken(accessToken);
        response.setRefreshToken(refreshToken.getToken());
        response.setUserId(userDetails.getId());
        return response;
    }

    @Transactional
    @Modifying
    public ResponseEntity<?> createNewUser(@RequestBody RegistrationUserDto registrationUserDto) {
        if (userService.findByEmail(registrationUserDto.getEmail()) != null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("User with this email already exists");
        }

        User user = userService.createNewUser(registrationUserDto);
        userSensitiveDataService.savePassword(user.getId(), registrationUserDto.getPassword());

        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(registrationUserDto.getEmail(), registrationUserDto.getPassword())
        );

        AuthResponseDto response = getTokens(user.getEmail());
        response.setUser(user);

        return ResponseEntity.ok(response);
    }
}

