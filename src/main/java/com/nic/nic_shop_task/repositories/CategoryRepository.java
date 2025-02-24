package com.nic.nic_shop_task.repositories;

import com.nic.nic_shop_task.models.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface CategoryRepository extends JpaRepository<Category, Long> {
    // Запрос для получения всех корневых категорий (без родителя)
    @Query("SELECT c FROM Category c WHERE c.parentCategory IS NULL")
    List<Category> findRootCategories();

    // Запрос для получения всех дочерних категорий конкретной категории
    @Query("SELECT c FROM Category c WHERE c.parentCategory.id = :parentId")
    List<Category> findChildrenByParentIdWithOutParent(Long parentId);

    @Query(value = "WITH RECURSIVE category_tree AS (" +
            "SELECT id FROM categories WHERE id = :categoryId " +
            "UNION ALL " +
            "SELECT c.id FROM categories c " +
            "INNER JOIN category_tree ct ON c.parent_category = ct.id" +
            ") SELECT * FROM category_tree", nativeQuery = true)
    List<Long> findAllSubCategoryIds(@Param("categoryId") Long categoryId);

    @Modifying
    @Query(value = "UPDATE categories " +
            "SET parent_category = :newParentCategoryId " +
            "WHERE parent_category = :parentCategoryId", nativeQuery = true)
    void updateParentCategories(
            @Param("parentCategoryId") Long parentCategoryId,
            @Param("newParentCategoryId") Long newParentCategoryId);

    @Modifying
    @Query(value = "UPDATE categories " +
            "SET parent_category = NULL " +
            "WHERE parent_category = :parentCategoryId", nativeQuery = true)
    void swapCategoriesToRootCategories(@Param("parentCategoryId") Long parentCategoryId);

    @Modifying
    @Query(value = "UPDATE categories " +
            "SET parent_category = :newParentId " +
            "WHERE id = :categoryId", nativeQuery = true)
    void updateCategoryParent(Long categoryId, Long newParentId);

    @Modifying
    @Query("UPDATE Category c SET c.name = :name WHERE c.id = :id")
    void updateCategory(Long id, String name);
}