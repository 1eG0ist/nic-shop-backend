package com.nic.nic_shop_task.services;

import com.nic.nic_shop_task.dtos.categories.UpdateCategoryDto;
import com.nic.nic_shop_task.models.Category;
import org.springframework.http.ResponseEntity;

public interface CategoryService {
    ResponseEntity<?> getCategoriesTreeS();

    ResponseEntity<?> getDefaultCategoriesS();

    ResponseEntity<Category> createCategoryS(Category category);

    ResponseEntity<?> updateCategoryS(UpdateCategoryDto category);

    ResponseEntity<?> deleteCategorySafeS(Long id);

    ResponseEntity<?> updateCategoryParentS(Long categoryId, Long newParentId);
}
