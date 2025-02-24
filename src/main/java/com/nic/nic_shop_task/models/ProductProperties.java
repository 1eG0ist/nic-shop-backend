package com.nic.nic_shop_task.models;

import lombok.Data;

import javax.persistence.*;

@Data
@Entity
@Table(name = "product_properties")
public class ProductProperties {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "product_id")
    private Product product;

    @ManyToOne
    @JoinColumn(name = "property_id")
    private Property property;

    private Double numericValue;
    private Double rangeStartValue;
    private Double rangeEndValue;
    private Boolean isValue;
    private String textValue;
}