package com.nic.nic_shop_task.controllers;

import com.nic.nic_shop_task.dtos.ProductRatingDto;
import com.nic.nic_shop_task.services.ProductRatingService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/products/rating")
@RequiredArgsConstructor
public class ProductRatingController {

    private final ProductRatingService productRatingService;

    @PostMapping
    ResponseEntity<?> addProductRating(@RequestBody ProductRatingDto productRatingDto) {
        return productRatingService.addProductRatingS(productRatingDto);
    }

}
