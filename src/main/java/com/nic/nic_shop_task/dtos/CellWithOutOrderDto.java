package com.nic.nic_shop_task.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public class CellWithOutOrderDto {
    private Long productId;
    private int count;
}
