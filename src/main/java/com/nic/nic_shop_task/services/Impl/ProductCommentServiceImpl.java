package com.nic.nic_shop_task.services.Impl;

import com.nic.nic_shop_task.models.ProductComment;
import com.nic.nic_shop_task.repositories.ProductCommentRepository;
import com.nic.nic_shop_task.repositories.UserRepository;
import com.nic.nic_shop_task.services.ImageService;
import com.nic.nic_shop_task.services.ProductCommentService;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.ZonedDateTime;
import java.util.Collections;
import java.util.Optional;


@Service
@AllArgsConstructor
public class ProductCommentServiceImpl implements ProductCommentService {
    private final ProductCommentRepository productCommentRepository;
    private final ImageService imageService;
    private final UserRepository userRepository;

    @Override
    public ResponseEntity<?> createProductCommentS(ProductComment productComment) {
        try {
            Long userId = Long.parseLong(SecurityContextHolder.getContext().getAuthentication().getName());
            productComment.setUser(userRepository.findUserById(userId).get());
            productComment.setCreatedDate(ZonedDateTime.now());
            return ResponseEntity.ok(productCommentRepository.save(productComment));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @Override
    public ResponseEntity<?> getProductCommentsByProductId(Long productId, Integer minRating, Integer maxRating, Integer page) {
        try {
            Pageable pageable = PageRequest.of(page, 10);
            Page<ProductComment> comments = productCommentRepository.findPageByProductIdWithFiltering(productId, minRating, maxRating, pageable);
            return ResponseEntity.ok(comments);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @Override
    public ResponseEntity<?> deleteProductCommentById(Long productCommentId) {
        try {
            Optional<ProductComment> comment = productCommentRepository.findById(productCommentId);
            if (!comment.isPresent()) return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
            String path = comment.get().getImagePath();
            productCommentRepository.delete(comment.get());
            if (path != null) {
                imageService.deleteFiles(Collections.singletonList(path));
            }
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @Override
    public ResponseEntity<?> deleteProductCommentImageByProductId(Long productCommentId) {
        try {
            Optional<ProductComment> comment = productCommentRepository.findById(productCommentId);
            if (!comment.isPresent()) return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
            String path = comment.get().getImagePath();
            comment.get().setImagePath(null);
            productCommentRepository.save(comment.get());
            if (path != null) {
                imageService.deleteFiles(Collections.singletonList(path));
            }
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
