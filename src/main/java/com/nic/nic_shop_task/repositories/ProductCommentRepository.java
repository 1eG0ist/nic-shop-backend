package com.nic.nic_shop_task.repositories;

import com.nic.nic_shop_task.models.ProductComment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;

@Repository
public interface ProductCommentRepository extends JpaRepository<ProductComment, Long> {

    @Query("SELECT pc FROM ProductComment pc WHERE pc.id = :productId " +
            "AND (:minRating IS NULL OR pc.rating >= :minRating) " +
            "AND (:maxRating IS NULL OR pc.rating <= :maxRating)")
    Page<ProductComment> findPageByProductIdWithFiltering(
            @Param("productId") Long productId,
            @Param("minRating") Integer minRating,
            @Param("maxRating") Integer maxRating,
            Pageable pageable);

    @Transactional
    @Modifying
    @Query(value = "DELETE FROM product_comments pc WHERE pc.id = :productCommentId RETURNING pc.image_path",
            nativeQuery = true)
    String deleteByIdWithReturning(@Param("productCommentId") Long productCommentId);

    @Transactional
    @Modifying
    @Query(value = "UPDATE product_comments pc SET image_path = null WHERE pc.id = :productCommentId RETURNING pc.image_path",
            nativeQuery = true)
    String deleteImageById(@Param("productCommentId") Long productCommentId);

}
