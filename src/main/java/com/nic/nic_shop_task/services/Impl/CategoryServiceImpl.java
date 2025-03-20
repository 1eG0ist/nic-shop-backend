package com.nic.nic_shop_task.services.Impl;

import com.nic.nic_shop_task.dtos.categories.UpdateCategoryDto;
import com.nic.nic_shop_task.models.Category;
import com.nic.nic_shop_task.repositories.CategoryRepository;
import com.nic.nic_shop_task.repositories.ProductRepository;
import com.nic.nic_shop_task.services.CategoryService;
import lombok.AllArgsConstructor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class CategoryServiceImpl implements CategoryService {

    private final CategoryRepository categoryRepository;
    private final ProductRepository productRepository;

    @Override
    public List<Category> getCategoriesTreeS() {
        List<Category> rootCategories = categoryRepository.findRootCategories();

        //TODO: recursive is a bad practice, rework in future with native SQL queries
        for (Category rootCategory : rootCategories) {
            fillChildren(rootCategory);
        }

        return rootCategories;
    }

    // Recursive finding child categories
    private void fillChildren(Category category) {
        List<Category> children = categoryRepository.findChildrenByParentIdWithOutParent(category.getId());

        // Stop infinite recursion
        for (Category el : children) {
            el.setParentCategory(null);
        }

        category.setChildren(children);

        for (Category child : children) {
            fillChildren(child);
        }
    }

    @Override
    public List<Category> getDefaultCategoriesS() {
        return categoryRepository.findAll();
    }

    @Override
    @Transactional
    @Modifying
    public Category createCategoryS(Category category) {
        return categoryRepository.save(category);
    }

    @Override
    @Transactional
    @Modifying
    public void updateCategoryS(UpdateCategoryDto category) {
        categoryRepository.updateCategory(category.getId(), category.getName());
    }

    @Override
    @Transactional
    @Modifying
    public Optional<Category> deleteCategorySafeS(Long id) {
        Optional<Category> optionalCategory = categoryRepository.findById(id);
        if (!optionalCategory.isPresent()) {
            return Optional.empty();
        }

        Category category = optionalCategory.get();
        Category parentCategory = category.getParentCategory();

        if (parentCategory == null) {
            productRepository.deleteCategoryFromProducts(category.getId());
            categoryRepository.swapCategoriesToRootCategories(category.getId());
        } else {
            productRepository.swapCategoryToDeletedCategoryParent(category.getId(), parentCategory.getId());
            categoryRepository.updateParentCategories(category.getId(), parentCategory.getId());
        }

        categoryRepository.deleteById(category.getId());

        return Optional.of(category);
    }

    @Override
    @Transactional
    @Modifying
    public void updateCategoryParentS(Long categoryId, Long newParentId) {
        categoryRepository.updateCategoryParent(categoryId, newParentId);
    }
}
