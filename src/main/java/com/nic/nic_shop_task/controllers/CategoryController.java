package com.nic.nic_shop_task.controllers;

import com.nic.nic_shop_task.dtos.categories.UpdateCategoryDto;
import com.nic.nic_shop_task.models.Category;
import com.nic.nic_shop_task.services.CategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/categories")
public class CategoryController {

    private final CategoryService categoryService;

    @GetMapping
    public ResponseEntity<List<Category>> getCategoriesTree() {
        try {
            return ResponseEntity.ok(categoryService.getCategoriesTreeS());
        } catch (Exception e) {
            return handleException(e);
        }
    }

    @GetMapping("/default")
    public ResponseEntity<List<Category>> getCategories() {
        try {
            return ResponseEntity.ok(categoryService.getDefaultCategoriesS());
        } catch (Exception e) {
            return handleException(e);
        }
    }

    @PostMapping("/admin")
    public ResponseEntity<Category> createCategory(@RequestBody Category category) {
        try {
            return ResponseEntity.status(HttpStatus.CREATED).body(categoryService.createCategoryS(category));
        } catch (Exception e) {
            return handleException(e);
        }
    }

    @PatchMapping("/admin")
    public ResponseEntity<Void> updateCategory(@RequestBody UpdateCategoryDto category) {
        try {
            categoryService.updateCategoryS(category);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            return handleException(e);
        }
    }

    @PatchMapping("/admin/parent")
    public ResponseEntity<Void> updateParentCategory(
            @RequestParam("categoryId") Long categoryId,
            @RequestParam(value = "newParentId", required = false) Long newParentId) {
        try {
            categoryService.updateCategoryParentS(categoryId, newParentId);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            return handleException(e);
        }
    }

    @DeleteMapping("/admin")
    public ResponseEntity<Void> deleteCategory(@RequestParam("id") Long id) {
        try {
            categoryService.deleteCategorySafeS(id);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            return handleException(e);
        }
    }

    /**
     * Глобальная обработка исключений внутри контроллера.
     * @param e исключение
     * @return ResponseEntity с кодом 400
     */
    private <T> ResponseEntity<T> handleException(Exception e) {
        return ResponseEntity.badRequest().build();
    }
}
