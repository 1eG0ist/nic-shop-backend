package com.nic.nic_shop_task.services.Impl;

import com.nic.nic_shop_task.configs.MyUserDetails;
import com.nic.nic_shop_task.dtos.RegistrationUserDto;
import com.nic.nic_shop_task.models.User;
import com.nic.nic_shop_task.repositories.UserRepository;
import com.nic.nic_shop_task.repositories.UserSensitiveDataRepository;
import com.nic.nic_shop_task.services.RoleService;
import com.nic.nic_shop_task.services.UserService;
import lombok.AllArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.ZoneOffset;
import java.util.Optional;

@Service
@AllArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final UserSensitiveDataRepository userSensitiveDataRepository;
    private final RoleService roleService;

    @Override
    public MyUserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Optional<User> user = userRepository.findUserByEmail(email);
        return user.map(u -> new MyUserDetails(u, userSensitiveDataRepository))
                .orElseThrow(() -> new UsernameNotFoundException(email + " not found"));
    }

    public MyUserDetails findByEmail(String email) {
        Optional<User> user = userRepository.findUserByEmail(email);
        return user.map(u -> new MyUserDetails(u, userSensitiveDataRepository))
                .orElse(null);
    }
    @Override
    public Optional<User> existsByEmail(String email) {
        return userRepository.findUserByEmail(email);
    }

    public User createNewUser(RegistrationUserDto registrationUserDto) {
        User user = new User(
                null,
                registrationUserDto.getName(),
                registrationUserDto.getSurname(),
                registrationUserDto.getEmail(),
                roleService.getUserRole());
        return userRepository.save(user);
    }
}
