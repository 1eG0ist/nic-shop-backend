package com.nic.nic_shop_task.models;

import javax.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name="user_sensitive_data")
public class UserSensitiveData {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "user_id", nullable = false)
    private Long userId;

    private String password;
}

