package com.nic.nic_shop_task.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.*;

@Data
@Entity
@Table(name = "product_properties")
@AllArgsConstructor
@NoArgsConstructor
public class ProductProperties {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @OnDelete(action= OnDeleteAction.CASCADE)
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