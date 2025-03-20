package com.nic.nic_shop_task.services.Impl;

import com.nic.nic_shop_task.dtos.CreateProductCommentDto;
import com.nic.nic_shop_task.models.Product;
import com.nic.nic_shop_task.models.ProductComment;
import com.nic.nic_shop_task.repositories.ProductCommentRepository;
import com.nic.nic_shop_task.repositories.ProductRepository;
import com.nic.nic_shop_task.repositories.UserRepository;
import com.nic.nic_shop_task.services.ImageService;
import com.nic.nic_shop_task.services.ProductCommentService;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.io.IOException;
import java.time.ZonedDateTime;
import java.util.Collections;
import java.util.List;

@Service
@AllArgsConstructor
public class ProductCommentServiceImpl implements ProductCommentService {
    private final ProductCommentRepository productCommentRepository;
    private final ImageService imageService;
    private final UserRepository userRepository;
    private final ProductRepository productRepository;

    @Override
    public ProductComment createProductComment(CreateProductCommentDto createProductCommentDto) {
        Long userId = Long.parseLong(SecurityContextHolder.getContext().getAuthentication().getName());

        Product product = productRepository.findById(createProductCommentDto.getProductId())
                .orElseThrow(() -> new RuntimeException("Product not found"));

        ProductComment productComment = new ProductComment();
        productComment.setProduct(product);
        productComment.setUser(userRepository.findUserById(userId)
                .orElseThrow(() -> new RuntimeException("User not found")));
        productComment.setCreatedDate(ZonedDateTime.now());
        productComment.setComment(createProductCommentDto.getComment());
        productComment.setRating(createProductCommentDto.getRating());
        productComment.setImagePath(createProductCommentDto.getImagePath());

        return productCommentRepository.save(productComment);
    }

    @Override
    public List<ProductComment> getProductCommentsByProductId(Long productId, Integer minRating, Integer maxRating, Integer page) {
        Pageable pageable = PageRequest.of(page, 10);
        Page<ProductComment> comments = productCommentRepository
                .findPageByProductIdWithFiltering(productId, minRating, maxRating, pageable);
        return comments.getContent();
    }

    @Override
    @Transactional
    public void deleteProductCommentById(Long productCommentId) throws IOException {
        ProductComment comment = productCommentRepository.findById(productCommentId)
                .orElseThrow(() -> new RuntimeException("Comment not found"));

        String path = comment.getImagePath();
        productCommentRepository.delete(comment);

        if (path != null) {
            imageService.deleteFiles(Collections.singletonList(path));
        }
    }

    @Override
    @Transactional
    public void deleteProductCommentImageByProductId(Long productCommentId) throws IOException {
        ProductComment comment = productCommentRepository.findById(productCommentId)
                .orElseThrow(() -> new RuntimeException("Comment not found"));

        String path = comment.getImagePath();
        comment.setImagePath(null);
        productCommentRepository.save(comment);

        if (path != null) {
            imageService.deleteFiles(Collections.singletonList(path));
        }
    }
}
