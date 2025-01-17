package com.nic.nic_shop_task.services;

import com.nic.nic_shop_task.configs.MyUserDetails;
import com.nic.nic_shop_task.dtos.RegistrationUserDto;
import com.nic.nic_shop_task.models.User;
import org.springframework.security.core.userdetails.UserDetailsService;

import java.util.Optional;

public interface UserService extends UserDetailsService {
    Optional<User> existsByEmail(String email);
    MyUserDetails findByEmail(String email);
    User createNewUser(RegistrationUserDto registrationUserDto);
}
