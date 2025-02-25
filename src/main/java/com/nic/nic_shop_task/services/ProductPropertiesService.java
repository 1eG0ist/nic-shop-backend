package com.nic.nic_shop_task.services;

import com.nic.nic_shop_task.dtos.ProductPropertyDto;
import com.nic.nic_shop_task.models.ProductProperties;
import org.springframework.http.ResponseEntity;

public interface ProductPropertiesService {
    ResponseEntity<?> createProductPropertyS(ProductPropertyDto productPropertyDto);

    ResponseEntity<?> updateProductPropertyS(ProductProperties productProperties);

    ResponseEntity<?> deleteProductPropertyS(Long id);
}
