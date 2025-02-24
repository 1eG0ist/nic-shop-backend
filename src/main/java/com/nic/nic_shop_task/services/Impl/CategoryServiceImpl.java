package com.nic.nic_shop_task.services.Impl;

import com.nic.nic_shop_task.dtos.categories.UpdateCategoryDto;
import com.nic.nic_shop_task.models.Category;
import com.nic.nic_shop_task.repositories.CategoryRepository;
import com.nic.nic_shop_task.repositories.ProductRepository;
import com.nic.nic_shop_task.services.CategoryService;
import lombok.AllArgsConstructor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.*;

@Service
@AllArgsConstructor
public class CategoryServiceImpl implements CategoryService {

    private final CategoryRepository categoryRepository;
    private final ProductRepository productRepository;

    @Override
    public ResponseEntity<List<Category>> getCategoriesTreeS() {
        List<Category> rootCategories = categoryRepository.findRootCategories();

        //TODO recursive is a bad practice, rework in future with native sql queries
        for (Category rootCategory : rootCategories) {
            fillChildren(rootCategory);
        }

        return ResponseEntity.ok(rootCategories);
    }

    // recursive finding child categories
    private void fillChildren(Category category) {
        List<Category> children = categoryRepository.findChildrenByParentIdWithOutParent(category.getId());

        // stop infinity recursive
        for (Category el: children) {
            el.setParentCategory(null);
        }

        category.setChildren(children);

        for (Category child : children) {
            fillChildren(child);
        }
    }

    @Override
    public ResponseEntity<?> getDefaultCategoriesS() {
        return ResponseEntity.ok(categoryRepository.findAll());
    }

    @Override
    @Transactional
    @Modifying
    public ResponseEntity<?> createCategoryS(Category category) {
        return ResponseEntity.status(HttpStatus.CREATED).body(categoryRepository.save(category));
    }

    @Override
    @Transactional
    @Modifying
    public ResponseEntity<?> updateCategoryS(UpdateCategoryDto category) {
        categoryRepository.updateCategory(category.getId(), category.getName());
        return ResponseEntity.ok().build();
    }

    @Override
    @Transactional
    @Modifying
    public ResponseEntity<?> deleteCategorySafeS(Long id) {
        Optional<Category> optionalCategory = categoryRepository.findById(id);
        if (!optionalCategory.isPresent()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }

        Category parentCategory = optionalCategory.get().getParentCategory();

        if (parentCategory == null) {
            productRepository.deleteCategoryFromProducts(optionalCategory.get().getId());
            categoryRepository.swapCategoriesToRootCategories(optionalCategory.get().getId());
        } else {
            productRepository.swapCategoryToDeletedCategoryParent(
                    optionalCategory.get().getId(), parentCategory.getId());
            categoryRepository.updateParentCategories(
                    optionalCategory.get().getId(), parentCategory.getId());
        }
        categoryRepository.deleteById(optionalCategory.get().getId());

        return ResponseEntity.ok().build();
    }

    @Override
    @Transactional
    @Modifying
    public ResponseEntity<?> updateCategoryParentS(Long categoryId, Long newParentId) {
        categoryRepository.updateCategoryParent(categoryId, newParentId);
        return ResponseEntity.ok().build();
    }
}
