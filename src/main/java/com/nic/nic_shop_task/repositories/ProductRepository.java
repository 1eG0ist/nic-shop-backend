package com.nic.nic_shop_task.repositories;

import com.nic.nic_shop_task.models.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {

    @Query(value = "SELECT p.* FROM products p " +
            "JOIN product_categories pc ON p.id = pc.product_id " +
            "WHERE pc.category_id IN :categoryIds",
            nativeQuery = true)
    Page<Product> findByCategoryIds(
            @Param("categoryIds") List<Long> categoryIds,
            Pageable pageable
    );
}
