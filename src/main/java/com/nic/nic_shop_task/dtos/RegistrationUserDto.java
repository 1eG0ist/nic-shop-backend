package com.nic.nic_shop_task.dtos;

import lombok.Data;

@Data
public class RegistrationUserDto {
    private String name;
    private String surname;
    private String email;
    private String password;
}
