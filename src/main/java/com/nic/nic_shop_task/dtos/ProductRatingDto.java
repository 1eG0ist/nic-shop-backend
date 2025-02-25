package com.nic.nic_shop_task.dtos;

import lombok.Data;

@Data
public class ProductRatingDto {
    private final Long productId;
    private final Integer rating;
}
