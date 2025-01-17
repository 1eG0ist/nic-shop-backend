package com.nic.nic_shop_task.configs;

import com.nic.nic_shop_task.models.User;
import com.nic.nic_shop_task.models.UserSensitiveData;
import com.nic.nic_shop_task.repositories.UserSensitiveDataRepository;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Optional;
import java.util.stream.Collectors;

public class MyUserDetails implements UserDetails {

    private final User user;
    private String password;

    public MyUserDetails(User user, UserSensitiveDataRepository userSensitiveDataRepository) {
        this.user = user;

        Optional<UserSensitiveData> userSensitiveData = userSensitiveDataRepository.findByUserId(user.getId());
        userSensitiveData.ifPresent(data -> this.password = data.getPassword());
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return user.getRoles().stream()
                .map(role -> new SimpleGrantedAuthority(role.getName()))
                .collect(Collectors.toList());
    }

    @Override
    public String getPassword() {
        return this.password;
    }

    @Override
    public String getUsername() {
        return user.getName();
    }

    public Long getId() {
        return user.getId();
    }

    public String getEmail() {
        return user.getEmail();
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
