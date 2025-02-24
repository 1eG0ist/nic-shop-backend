package com.nic.nic_shop_task.controllers;

import com.nic.nic_shop_task.dtos.categories.UpdateCategoryDto;
import com.nic.nic_shop_task.models.Category;
import com.nic.nic_shop_task.services.CategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController()
@RequiredArgsConstructor
@RequestMapping("/categories")
public class CategoryController {

    private final CategoryService categoryService;

    @GetMapping
    public ResponseEntity<?> getCategoriesTree() {
        return categoryService.getCategoriesTreeS();
    }

    @GetMapping("/default")
    public ResponseEntity<?> getCategories() {
        return categoryService.getDefaultCategoriesS();
    }

    @PostMapping("/admin")
    public ResponseEntity<?> createCategory(@RequestBody Category category) {
        return categoryService.createCategoryS(category);
    }

    @PatchMapping("/admin")
    public ResponseEntity<?> updateCategory(@RequestBody UpdateCategoryDto category) {
        return categoryService.updateCategoryS(category);
    }

    @PatchMapping("/admin/parent")
    public ResponseEntity<?> updateParentCategory(
            @RequestParam("categoryId") Long categoryId,
            @RequestParam(value = "newParentId", required = false) Long newParentId) {
        return categoryService.updateCategoryParentS(categoryId, newParentId);
    }

    @DeleteMapping("/admin")
    public ResponseEntity<?> deleteCategory(@RequestParam("id") Long id) {
        return categoryService.deleteCategorySafeS(id);
    }

}
