package com.nic.nic_shop_task.services;

import com.nic.nic_shop_task.models.RefreshToken;

import java.util.Optional;

public interface RefreshTokenService {
    RefreshToken createRefreshToken(String email);
    Optional<RefreshToken> findByToken(String token);
    Optional<RefreshToken> verifyExpiration(RefreshToken token);
    Optional<RefreshToken> findByUserId(Long userId);

}
