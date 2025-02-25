package com.nic.nic_shop_task.repositories;

import com.nic.nic_shop_task.models.ProductRating;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductRatingRepository extends JpaRepository<ProductRating, Long> {

    @Query(value = "INSERT INTO product_rating (creator_id, product_id, rating) " +
            "VALUES (:userId, :productId, :rating) ", nativeQuery = true)
    void saveNewRating(Long userId, Long productId, Integer rating);
}
