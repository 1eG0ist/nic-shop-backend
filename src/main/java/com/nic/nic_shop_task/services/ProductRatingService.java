package com.nic.nic_shop_task.services;

import com.nic.nic_shop_task.dtos.ProductRatingDto;
import org.springframework.http.ResponseEntity;

public interface ProductRatingService {
    ResponseEntity<?> addProductRatingS(ProductRatingDto productRatingDto);
}
