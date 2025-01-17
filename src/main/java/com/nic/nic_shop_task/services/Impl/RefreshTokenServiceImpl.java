package com.nic.nic_shop_task.services.Impl;

import com.nic.nic_shop_task.models.RefreshToken;
import com.nic.nic_shop_task.repositories.RefreshTokenRepository;
import com.nic.nic_shop_task.repositories.UserRepository;
import com.nic.nic_shop_task.services.RefreshTokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.Instant;
import java.util.Date;
import java.util.Optional;
import java.util.UUID;

@Service
public class RefreshTokenServiceImpl implements RefreshTokenService {

    @Autowired
    RefreshTokenRepository refreshTokenRepository;

    @Autowired
    UserRepository userRepository;

    @Value("${jwt.refresh_lifetime}")
    private Duration refreshLifetime;

    public RefreshToken createRefreshToken(String email){
        Date expiredDate = new Date(new Date().getTime() + refreshLifetime.toMillis());
        RefreshToken refreshToken = RefreshToken.builder()
                .user(userRepository.findUserByEmail(email).get())
                .token(UUID.randomUUID().toString())
                .expiryDate(expiredDate.toInstant())
                .build();
        return refreshTokenRepository.save(refreshToken);
    }

    public Optional<RefreshToken> findByToken(String token) {
        return refreshTokenRepository.findByToken(token);
    }

    public Optional<RefreshToken> verifyExpiration(RefreshToken token) {
        if(token.getExpiryDate().compareTo(Instant.now())<0){
            refreshTokenRepository.delete(token);
            return Optional.empty();
        }
        return Optional.of(token);
    }

    public Optional<RefreshToken> findByUserId(Long userId) {
        return refreshTokenRepository.findByUserId(userId);
    };

}