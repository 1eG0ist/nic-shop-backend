package com.nic.nic_shop_task.services.Impl;

import com.nic.nic_shop_task.dtos.CellWithOutOrderDto;
import com.nic.nic_shop_task.models.Product;
import com.nic.nic_shop_task.repositories.CategoryRepository;
import com.nic.nic_shop_task.repositories.ProductRepository;
import com.nic.nic_shop_task.services.ProductService;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.NoSuchElementException;

@AllArgsConstructor
@Service
public class ProductServiceImpl implements ProductService {
    private final CategoryRepository categoryRepository;
    private final ProductRepository productRepository;

    @Override
    public ResponseEntity<?> getProductsS(Long categoryId, String sortBy, Integer page) {
        try {
            Sort sort;
            List<Long> categories = categoryRepository.findAllSubCategoryIds(categoryId);
            sortBy = sortBy == null ? "null" : sortBy.toLowerCase();
            switch (sortBy) {
                case "null":
                    sort = Sort.by(Sort.Direction.ASC, "id");
                    break;
                case "asc":
                    sort = Sort.by(Sort.Direction.ASC, "price");
                    break;
                case "desc":
                    sort = Sort.by(Sort.Direction.DESC, "price");
                    break;
                default:
                    throw new IllegalArgumentException("Invalid sortBy value: " + sortBy);
            }

            Pageable pageable = PageRequest.of(page, 10, sort);
            Page<Product> products = productRepository.findByCategoryIds(categories, pageable);
            return ResponseEntity.ok(products);
        } catch (Exception ex) {
            return ResponseEntity.badRequest().body(ex.getMessage());
        }
    }

    @Override
    public ResponseEntity<Product> getProductS(Long productId) {
        try {
            return productRepository.findById(productId)
                    .map(ResponseEntity::ok)
                    .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).build());
        } catch (Exception ex) {
            return ResponseEntity.badRequest().build();
        }
    }

    @Override
    public ResponseEntity<Integer> checkProductsCountS(Long productId) {
        Integer count = productRepository.findById(productId)
                .map(Product::getCount)
                .orElseThrow(() -> new NoSuchElementException("Product not found with id: " + productId));

        return ResponseEntity.ok(count);
    }

    @Transactional
    public void checkAndReduceProductQuantity(List<CellWithOutOrderDto> cells) throws NoSuchElementException, IllegalArgumentException {
        for (CellWithOutOrderDto cell : cells) {
            Product product = productRepository.findById(cell.getProductId())
                    .orElseThrow(() -> new NoSuchElementException(String.format("Product not found with id %d ", cell.getProductId())));

            if (product.getCount() == 0) {
                throw new IllegalArgumentException(String.format("No product %s with an id %d in the warehouses",
                        product.getName(),
                        product.getId()));
            } else if (product.getCount() < cell.getCount()) {
                throw new IllegalArgumentException(String.format(
                        "Have only %d products %s out of %d",
                        product.getCount(),
                        product.getName(),
                        cell.getCount()));
            }

            product.setCount(product.getCount() - cell.getCount());
            productRepository.save(product);
        }
    }

    @Override
    public List<Product> getProductsByIds(List<Long> ids) {
        return productRepository.findProductsByIds(ids);
    }
}
