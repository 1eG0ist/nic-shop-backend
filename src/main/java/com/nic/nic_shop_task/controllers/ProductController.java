package com.nic.nic_shop_task.controllers;

import com.nic.nic_shop_task.dtos.FilterPropertyDto;
import com.nic.nic_shop_task.models.Product;
import com.nic.nic_shop_task.services.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

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
        try {
            Page<Product> products = productService.getProducts(
                    categoryId, sort, minRating, maxRating, page,
                    filterProperties == null ? new ArrayList<>() : filterProperties
            );
            return ResponseEntity.ok(products);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @GetMapping
    public ResponseEntity<?> getProduct(@RequestParam("id") Long productId) {
        try {
            Product product = productService.getProduct(productId);
            return ResponseEntity.ok(product);
        } catch (NoSuchElementException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    @GetMapping("/check")
    public ResponseEntity<?> checkProductCount(@RequestParam("id") Long productId) {
        try {
            Integer count = productService.checkProductsCount(productId);
            return ResponseEntity.ok(count);
        } catch (NoSuchElementException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    @PostMapping("/admin")
    public ResponseEntity<?> createProduct(@RequestBody Product product) {
        try {
            Product createdProduct = productService.createProduct(product);
            return ResponseEntity.status(HttpStatus.CREATED).body(createdProduct);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @PatchMapping("/admin")
    public ResponseEntity<?> updateProduct(@RequestBody Product product) {
        try {
            Product updatedProduct = productService.updateProduct(product);
            return ResponseEntity.ok(updatedProduct);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @DeleteMapping("/admin")
    public ResponseEntity<?> deleteProduct(@RequestParam("id") Long id) {
        try {
            productService.deleteProduct(id);
            return ResponseEntity.ok().build();
        } catch (IOException e) {
            return ResponseEntity.internalServerError().body("Failed to delete product files: " + e.getMessage());
        } catch (NoSuchElementException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Product not found: " + e.getMessage());
        }
    }
}
