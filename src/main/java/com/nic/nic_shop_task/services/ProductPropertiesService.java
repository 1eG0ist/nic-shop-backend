package com.nic.nic_shop_task.services;

import com.nic.nic_shop_task.dtos.ProductPropertyDto;
import com.nic.nic_shop_task.models.ProductProperties;

public interface ProductPropertiesService {
    ProductProperties createProductPropertyS(ProductPropertyDto productPropertyDto) throws RuntimeException;
    ProductProperties updateProductPropertyS(ProductProperties productProperties);
    void deleteProductPropertyS(Long id) throws RuntimeException;
}
