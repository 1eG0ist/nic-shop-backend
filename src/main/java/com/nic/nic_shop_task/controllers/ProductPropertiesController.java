package com.nic.nic_shop_task.controllers;

import com.nic.nic_shop_task.dtos.ProductPropertyDto;
import com.nic.nic_shop_task.models.ProductProperties;
import com.nic.nic_shop_task.services.ProductPropertiesService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("product_properties/admin")
public class ProductPropertiesController {

    private final ProductPropertiesService productPropertiesService;

    @PostMapping
    public ResponseEntity<?> createProductProperty(@RequestBody ProductPropertyDto productPropertyDto) {
        return productPropertiesService.createProductPropertyS(productPropertyDto);
    }

    @PatchMapping
    public ResponseEntity<?> updateProductProperty(@RequestBody ProductProperties productProperties) {
        return productPropertiesService.updateProductPropertyS(productProperties);
    }

    @DeleteMapping
    public ResponseEntity<?> deleteProductProperty(@RequestParam("id") Long id) {
        return productPropertiesService.deleteProductPropertyS(id);
    }
}
