package com.nic.nic_shop_task.controllers;

import com.nic.nic_shop_task.models.Product;
import com.nic.nic_shop_task.services.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/products")
public class ProductController {
    private final ProductService productService;

    @GetMapping("/by_category")
    public ResponseEntity<?> getProducts(
            @RequestParam("id") Long categoryId,
            @RequestParam(value = "sort", required = false) String sort,
            @RequestParam("page") Integer page) {
        return productService.getProductsS(categoryId, sort, page);
    }

    @GetMapping
    public ResponseEntity<Product> getProduct(@RequestParam("id") Long productId) {
        return productService.getProductS(productId);
    }
}
