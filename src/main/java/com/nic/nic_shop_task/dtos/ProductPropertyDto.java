package com.nic.nic_shop_task.dtos;

import com.nic.nic_shop_task.models.Property;
import lombok.Data;

@Data
public class ProductPropertyDto {
    private Property property;
    private Long productId;
    private Double numericValue;
    private Double rangeStartValue;
    private Double rangeEndValue;
    private Boolean isValue;
    private String textValue;
}
