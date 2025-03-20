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
        try {
            ProductProperties createdProductProperties = productPropertiesService.createProductPropertyS(productPropertyDto);
            return ResponseEntity.ok(createdProductProperties);
        } catch (RuntimeException e) {
            return ResponseEntity.status(400).body(e.getMessage());
        }
    }

    @PatchMapping
    public ResponseEntity<?> updateProductProperty(@RequestBody ProductProperties productProperties) {
        try {
            ProductProperties updatedProductProperties = productPropertiesService.updateProductPropertyS(productProperties);
            return ResponseEntity.ok(updatedProductProperties);
        } catch (RuntimeException e) {
            return ResponseEntity.status(400).body(e.getMessage());
        }
    }

    @DeleteMapping
    public ResponseEntity<?> deleteProductProperty(@RequestParam("id") Long id) {
        try {
            productPropertiesService.deleteProductPropertyS(id);
            return ResponseEntity.ok().build();
        } catch (RuntimeException e) {
            return ResponseEntity.status(400).body(e.getMessage());
        }
    }
}
