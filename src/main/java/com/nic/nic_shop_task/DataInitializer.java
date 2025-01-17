package com.nic.nic_shop_task;

import com.nic.nic_shop_task.models.Role;
import com.nic.nic_shop_task.repositories.RoleRepository;
import lombok.AllArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;

@Service
@AllArgsConstructor
public class DataInitializer implements CommandLineRunner {

    private RoleRepository roleRepository;

    @Override
    public void run(String... args) throws Exception {
        List<String> requiredRoles = Arrays.asList(
                "ROLE_USER",
                "ROLE_ADMIN"
        );

        for (String roleName : requiredRoles) {
            if (!roleRepository.findByName(roleName).isPresent()) {
                Role role = new Role();
                role.setName(roleName);
                roleRepository.save(role);
            }
        }
    }
}
