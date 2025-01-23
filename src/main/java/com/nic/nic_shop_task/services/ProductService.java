package com.nic.nic_shop_task.services;

import com.nic.nic_shop_task.models.Product;
import org.springframework.http.ResponseEntity;

public interface ProductService {
    ResponseEntity<?> getProductsS(Long categoryId, String sort, Integer page);
    ResponseEntity<Product> getProductS(Long productId);
}
