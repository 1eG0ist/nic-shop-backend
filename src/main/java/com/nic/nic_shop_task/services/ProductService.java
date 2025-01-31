package com.nic.nic_shop_task.services;

import com.nic.nic_shop_task.dtos.CellWithOutOrderDto;
import com.nic.nic_shop_task.models.Product;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface ProductService {
    ResponseEntity<?> getProductsS(Long categoryId, String sort, Integer page);

    ResponseEntity<Product> getProductS(Long productId);

    ResponseEntity<Integer> checkProductsCountS(Long productId);
    void checkAndReduceProductQuantity(List<CellWithOutOrderDto> cells);
    List<Product> getProductsByIds(List<Long> ids);
}
