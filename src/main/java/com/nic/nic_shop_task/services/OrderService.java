package com.nic.nic_shop_task.services;

import com.nic.nic_shop_task.dtos.CellWithOutOrderDto;
import com.nic.nic_shop_task.models.Order;

import java.util.List;

public interface OrderService {
    Order createOrder(List<CellWithOutOrderDto> cells) throws Exception;
}
