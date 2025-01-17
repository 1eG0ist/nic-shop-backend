package com.nic.nic_shop_task.services.Impl;

import com.nic.nic_shop_task.models.Role;
import com.nic.nic_shop_task.repositories.RoleRepository;
import com.nic.nic_shop_task.services.RoleService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;


@Service
@AllArgsConstructor
public class RoleServiceImpl implements RoleService {
    private final RoleRepository roleRepository;

    @Override
    public List<Role> getUserRole() {
        Role role = roleRepository.findByName("ROLE_USER").get();
        List<Role> roles = new ArrayList<>();
        roles.add(role);
        return roles;
    }
}
