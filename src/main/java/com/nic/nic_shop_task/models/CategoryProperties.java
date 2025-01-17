package com.nic.nic_shop_task.models;

import lombok.Data;

import javax.persistence.*;

@Data
@Entity
@Table(name = "category_properties")
public class CategoryProperties {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "category_id")
    private Category category;

    @ManyToOne
    @JoinColumn(name = "property_id")
    private Property property;

    private String name;
    private Double valueValue;
    private Double rangeStartValue;
    private Double rangeEndValue;
    private Boolean isValue;
    private String textValue;
}
