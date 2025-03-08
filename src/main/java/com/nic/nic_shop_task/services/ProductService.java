package com.nic.nic_shop_task.services;

import com.nic.nic_shop_task.dtos.CellWithOutOrderDto;
import com.nic.nic_shop_task.dtos.FilterPropertyDto;
import com.nic.nic_shop_task.models.Product;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface ProductService {
    ResponseEntity<?> getProductsS(
            Long categoryId,
            String sort,
            Double minRating,
            Double maxRating,
            Integer page,
            List<FilterPropertyDto> filterProperties);

    ResponseEntity<Product> getProductS(Long productId);

    ResponseEntity<Integer> checkProductsCountS(Long productId);

    ResponseEntity<?> checkAndReduceProductQuantity(List<CellWithOutOrderDto> cells);

    List<Product> getProductsByIds(List<Long> ids);

    ResponseEntity<?> createProductS(Product product);

    ResponseEntity<?> updateProductS(Product product);

    ResponseEntity<?> deleteProduct(Long id);
}
