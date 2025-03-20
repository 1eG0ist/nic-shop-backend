package com.nic.nic_shop_task.services;

import com.nic.nic_shop_task.dtos.categories.UpdateCategoryDto;
import com.nic.nic_shop_task.models.Category;

import java.util.List;
import java.util.Optional;

public interface CategoryService {
    List<Category> getCategoriesTreeS();

    List<Category> getDefaultCategoriesS();

    Category createCategoryS(Category category);

    void updateCategoryS(UpdateCategoryDto category);

    Optional<Category> deleteCategorySafeS(Long id);

    void updateCategoryParentS(Long categoryId, Long newParentId);
}
