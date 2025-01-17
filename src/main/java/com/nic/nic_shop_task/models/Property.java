package com.nic.nic_shop_task.models;

import lombok.Data;

import javax.persistence.*;

@Data
@Entity
@Table(name = "properties")
public class Property {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String propertyType;
}
