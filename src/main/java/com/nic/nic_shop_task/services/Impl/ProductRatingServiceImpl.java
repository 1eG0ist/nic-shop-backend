package com.nic.nic_shop_task.services.Impl;

import com.nic.nic_shop_task.dtos.ProductRatingDto;
import com.nic.nic_shop_task.repositories.ProductRatingRepository;
import com.nic.nic_shop_task.repositories.ProductRepository;
import com.nic.nic_shop_task.services.ProductRatingService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Service
@RequiredArgsConstructor
public class ProductRatingServiceImpl implements ProductRatingService {
    private final ProductRatingRepository productRatingRepository;
    private final ProductRepository productRepository;
    @Override
    @Transactional
    @Modifying
    public void addProductRatingS(ProductRatingDto productRatingDto) {
        Long userId = Long.parseLong(SecurityContextHolder.getContext().getAuthentication().getName());
        productRatingRepository.saveNewRating(userId, productRatingDto.getProductId(), productRatingDto.getRating());
        productRepository.addRating(productRatingDto.getProductId(), productRatingDto.getRating());
    }
}
