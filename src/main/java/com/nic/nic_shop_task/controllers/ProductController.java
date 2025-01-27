package com.nic.nic_shop_task.controllers;

import com.nic.nic_shop_task.dtos.Errors.ErrorDetail;
import com.nic.nic_shop_task.models.Product;
import com.nic.nic_shop_task.services.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.Locale;
import java.util.NoSuchElementException;

@RestController
@RequiredArgsConstructor
@RequestMapping("/products")
public class ProductController {

    private final ProductService productService;

    private final MessageSource messageSource;

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

    @GetMapping("/check")
    public ResponseEntity<Integer> checkProductCount(@RequestParam("id") Long productId) {
        return productService.checkProductsCountS(productId);
    }

    @ExceptionHandler(NoSuchElementException.class)
    public ResponseEntity<ErrorDetail> handleNoSuchElementException(NoSuchElementException exception, Locale locale) {
        String title = messageSource.getMessage("errors.404.title", new Object[0], "errors.404.title", locale);
        String detail = messageSource.getMessage(exception.getMessage(), new Object[0], exception.getMessage(), locale);

        ErrorDetail errorDetail = new ErrorDetail(title, HttpStatus.NOT_FOUND.value(), detail, Collections.emptyList());

        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorDetail);
    }
}
