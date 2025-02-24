package com.nic.nic_shop_task.dtos;

import lombok.Data;

@Data
public class FilterPropertyDto {
    private Long propertyId;
    private Double numericValue;
    private Double minRangeValue;
    private Double maxRangeValue;
    private String textValue;
    private Boolean booleanValue;
}