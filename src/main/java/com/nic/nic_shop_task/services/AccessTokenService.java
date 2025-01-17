package com.nic.nic_shop_task.services;

import com.nic.nic_shop_task.configs.MyUserDetails;

import java.util.List;

public interface AccessTokenService {
    String generateToken(MyUserDetails userDetails);
    String getUserEmail(String token);
    String getUserId(String token);
    List<String> getRoles(String token);
}
