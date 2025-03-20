package com.nic.nic_shop_task.services;

import com.nic.nic_shop_task.dtos.CreateProductCommentDto;
import com.nic.nic_shop_task.models.ProductComment;

import java.io.IOException;
import java.util.List;

public interface ProductCommentService {
    ProductComment createProductComment(CreateProductCommentDto createProductCommentDto);
    List<ProductComment> getProductCommentsByProductId(Long productId, Integer minRating, Integer maxRating, Integer page);
    void deleteProductCommentById(Long productCommentId) throws IOException;
    void deleteProductCommentImageByProductId(Long productCommentId) throws IOException;
}
