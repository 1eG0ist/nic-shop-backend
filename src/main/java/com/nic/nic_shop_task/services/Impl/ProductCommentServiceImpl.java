package com.nic.nic_shop_task.services.Impl;

import com.nic.nic_shop_task.models.ProductComment;
import com.nic.nic_shop_task.repositories.ProductCommentRepository;
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


@Service
@AllArgsConstructor
public class ProductCommentServiceImpl implements ProductCommentService {
    private final ProductCommentRepository productCommentRepository;
    private final ImageService imageService;

    @Override
    public ResponseEntity<?> createProductComment(ProductComment productComment) {
        try {
            boolean hasUserRole = SecurityContextHolder
                    .getContext()
                    .getAuthentication()
                    .getAuthorities()
                    .stream()
                    .anyMatch(role -> role.getAuthority().equals("ROLE_USER"));

            if (!hasUserRole) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
            }
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
            if (!isAdmin()) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
            }

            String path = productCommentRepository.deleteByIdWithReturning(productCommentId);
            imageService.deleteFiles(Collections.singletonList(path));
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @Override
    public ResponseEntity<?> deleteProductCommentImageByProductId(Long productCommentId) {
        try {
            if (!isAdmin()) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
            }

            String imagePathToDelete = productCommentRepository.deleteImageById(productCommentId);
            imageService.deleteFiles(Collections.singletonList(imagePathToDelete));
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    boolean isAdmin() {
        return SecurityContextHolder
                .getContext()
                .getAuthentication()
                .getAuthorities()
                .stream()
                .anyMatch(role -> role.getAuthority().equals("ROLE_ADMIN"));

    }
}
