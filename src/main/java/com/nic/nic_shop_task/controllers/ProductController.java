package com.nic.nic_shop_task.controllers;

import com.nic.nic_shop_task.dtos.FilterPropertyDto;
import com.nic.nic_shop_task.models.Product;
import com.nic.nic_shop_task.services.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/products")
public class ProductController {

    private final ProductService productService;

    @PostMapping("/by_category")
    public ResponseEntity<?> getProducts(
            @RequestParam("id") Long categoryId,
            @RequestParam(value = "sort", defaultValue = "asc") String sort,
            @RequestParam(value = "minRating", defaultValue = "1") Double minRating,
            @RequestParam(value = "maxRating", defaultValue = "5") Double maxRating,
            @RequestParam("page") Integer page,
            @RequestBody(required = false) List<FilterPropertyDto> filterProperties) {
        return productService.getProductsS(
                categoryId,
                sort,
                minRating,
                maxRating,
                page,
                filterProperties == null ? new ArrayList<>() : filterProperties);
    }

    @GetMapping
    public ResponseEntity<Product> getProduct(@RequestParam("id") Long productId) {
        return productService.getProductS(productId);
    }

    @GetMapping("/check")
    public ResponseEntity<Integer> checkProductCount(@RequestParam("id") Long productId) {
        return productService.checkProductsCountS(productId);
    }

    @PostMapping("/admin")
    public ResponseEntity<?> createProduct(@RequestBody Product product) {
        return productService.createProductS(product);
    }

    @PatchMapping("/admin")
    public ResponseEntity<?> updateProduct(@RequestBody Product product) {
        return productService.updateProductS(product);
    }

    @DeleteMapping("/admin")
    public ResponseEntity<?> deleteProduct(@RequestParam("id") Long id) {
        return productService.deleteProduct(id);
    }
}
