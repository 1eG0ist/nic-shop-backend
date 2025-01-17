package com.nic.nic_shop_task.models;

import lombok.Data;

import javax.persistence.*;

@Data
@Entity
@Table(name = "products")
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String description;
    private Double price;
    private Integer count;
    private String imagePath;
    private Integer sale;

    @ManyToOne
    @JoinColumn(name = "company_id")
    private Company company;
}
