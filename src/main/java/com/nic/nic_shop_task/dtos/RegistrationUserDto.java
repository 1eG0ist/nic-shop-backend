package com.nic.nic_shop_task.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RegistrationUserDto {
    private String name;
    private String surname;
    private String email;
    private String password;
}
