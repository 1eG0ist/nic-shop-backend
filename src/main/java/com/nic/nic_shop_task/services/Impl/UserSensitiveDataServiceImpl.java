package com.nic.nic_shop_task.services.Impl;

import com.nic.nic_shop_task.models.UserSensitiveData;
import com.nic.nic_shop_task.repositories.UserSensitiveDataRepository;
import com.nic.nic_shop_task.services.UserSensitiveDataService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserSensitiveDataServiceImpl implements UserSensitiveDataService {
    private final UserSensitiveDataRepository userSensitiveDataRepository;
    private final PasswordEncoder passwordEncoder;

    public void savePassword(Long userId, String password) {
        UserSensitiveData sensitiveData = new UserSensitiveData();
        sensitiveData.setUserId(userId);
        sensitiveData.setPassword(passwordEncoder.encode(password));
        userSensitiveDataRepository.save(sensitiveData);
    }
}
