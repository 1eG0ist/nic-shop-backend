package com.nic.nic_shop_task.services;

import com.nic.nic_shop_task.dtos.CellWithOutOrderDto;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface OrderService {
    ResponseEntity<?> createOrderS(List<CellWithOutOrderDto> cells);
}
