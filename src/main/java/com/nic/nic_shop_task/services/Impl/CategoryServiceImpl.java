package com.nic.nic_shop_task.services.Impl;

import com.nic.nic_shop_task.models.Category;
import com.nic.nic_shop_task.repositories.CategoryRepository;
import com.nic.nic_shop_task.services.CategoryService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@AllArgsConstructor
public class CategoryServiceImpl implements CategoryService {

    private final CategoryRepository categoryRepository;

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

}
