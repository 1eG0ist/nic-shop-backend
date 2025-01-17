package com.nic.nic_shop_task.services;

public interface UserSensitiveDataService {
    void savePassword(Long userId, String password);
}
