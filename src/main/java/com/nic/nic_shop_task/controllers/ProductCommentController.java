package com.nic.nic_shop_task.controllers;

import com.nic.nic_shop_task.models.ProductComment;
import com.nic.nic_shop_task.services.ProductCommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/product_comments")
@RequiredArgsConstructor
public class ProductCommentController {
    private final ProductCommentService productCommentService;

    @PostMapping()
    public ResponseEntity<?> createProductComment(@RequestBody ProductComment productComment) {
        return productCommentService.createProductCommentS(productComment);
    }

    @GetMapping("/all")
    public ResponseEntity<?> getProductCommentsByProductId(
            @RequestParam("id") Long productId,
            @RequestParam(value = "minRating", defaultValue = "1") Integer minRating,
            @RequestParam(value = "maxRating", defaultValue = "5") Integer maxRating,
            @RequestParam("page") Integer page) {
        return productCommentService.getProductCommentsByProductId(productId, minRating, maxRating, page);
    }

    @DeleteMapping("/admin")
    public ResponseEntity<?> deleteProductCommentById(@RequestParam("id") Long productCommentId) {
        return productCommentService.deleteProductCommentById(productCommentId);
    }

    @DeleteMapping("/admin/image")
    public ResponseEntity<?> deleteProductCommentImageByProductId(@RequestParam("id") Long productCommentId) {
        return productCommentService.deleteProductCommentImageByProductId(productCommentId);
    }
}
