package com.nic.nic_shop_task.controllers;

import com.nic.nic_shop_task.dtos.CreateProductCommentDto;
import com.nic.nic_shop_task.models.ProductComment;
import com.nic.nic_shop_task.services.ProductCommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/product_comments")
@RequiredArgsConstructor
public class ProductCommentController {
    private final ProductCommentService productCommentService;

    @PostMapping()
    public ResponseEntity<?> createProductComment(@RequestBody CreateProductCommentDto createProductCommentDto) {
        try {
            ProductComment comment = productCommentService.createProductComment(createProductCommentDto);
            return ResponseEntity.ok(comment);
        } catch (RuntimeException e) {
            return ResponseEntity.status(400).body(e.getMessage());
        }
    }

    @GetMapping("/all")
    public ResponseEntity<?> getProductCommentsByProductId(
            @RequestParam("id") Long productId,
            @RequestParam(value = "minRating", defaultValue = "1") Integer minRating,
            @RequestParam(value = "maxRating", defaultValue = "5") Integer maxRating,
            @RequestParam("page") Integer page) {
        try {
            List<ProductComment> comments = productCommentService.getProductCommentsByProductId(productId, minRating, maxRating, page);
            return ResponseEntity.ok(comments);
        } catch (RuntimeException e) {
            return ResponseEntity.status(400).body(e.getMessage());
        }
    }

    @DeleteMapping("/admin")
    public ResponseEntity<?> deleteProductCommentById(@RequestParam("id") Long productCommentId) {
        try {
            productCommentService.deleteProductCommentById(productCommentId);
            return ResponseEntity.ok().build();
        } catch (RuntimeException e) {
            return ResponseEntity.status(400).body(e.getMessage());
        } catch (IOException e) {
            return ResponseEntity.status(500).body("Error by deleting comment image");
        }
    }

    @DeleteMapping("/admin/image")
    public ResponseEntity<?> deleteProductCommentImageByProductId(@RequestParam("id") Long productCommentId) {
        try {
            productCommentService.deleteProductCommentImageByProductId(productCommentId);
            return ResponseEntity.ok().build();
        } catch (RuntimeException e) {
            return ResponseEntity.status(400).body(e.getMessage());
        } catch (IOException e) {
            return ResponseEntity.status(500).body("Error by deleting comment image");
        }
    }
}
