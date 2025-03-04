package com.nic.nic_shop_task.services;


import com.nic.nic_shop_task.models.ProductComment;
import org.springframework.http.ResponseEntity;

public interface ProductCommentService {
    ResponseEntity<?> createProductCommentS(ProductComment productComment);
    ResponseEntity<?> getProductCommentsByProductId(Long productId, Integer minRating, Integer maxRating, Integer page);
    ResponseEntity<?> deleteProductCommentById(Long productCommentId);
    ResponseEntity<?> deleteProductCommentImageByProductId(Long productCommentId);
}
