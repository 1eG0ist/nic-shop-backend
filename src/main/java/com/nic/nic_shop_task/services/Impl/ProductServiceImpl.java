package com.nic.nic_shop_task.services.Impl;

import com.nic.nic_shop_task.dtos.CellWithOutOrderDto;
import com.nic.nic_shop_task.dtos.FilterPropertyDto;
import com.nic.nic_shop_task.models.Product;
import com.nic.nic_shop_task.models.ProductComment;
import com.nic.nic_shop_task.repositories.CategoryRepository;
import com.nic.nic_shop_task.repositories.ProductCommentRepository;
import com.nic.nic_shop_task.repositories.ProductRepository;
import com.nic.nic_shop_task.services.ImageService;
import com.nic.nic_shop_task.services.ProductService;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

@AllArgsConstructor
@Service
public class ProductServiceImpl implements ProductService {
    private final CategoryRepository categoryRepository;
    private final ProductRepository productRepository;
    private final ProductCommentRepository productCommentRepository;
    private final ImageService imageService;

    @Override
    public Page<Product> getProducts(
            Long categoryId,
            String sortBy,
            Double minRating,
            Double maxRating,
            Integer page,
            List<FilterPropertyDto> filterProperties) {

        Sort sort;
        List<Long> categories = categoryRepository.findAllSubCategoryIds(categoryId);
        sortBy = sortBy == null ? "null" : sortBy.toLowerCase();
        switch (sortBy) {
            case "asc":
                sort = Sort.by(Sort.Direction.ASC, "price");
                break;
            case "desc":
                sort = Sort.by(Sort.Direction.DESC, "price");
                break;
            default:
                sort = Sort.by(Sort.Direction.ASC, "id");
        }

        Pageable pageable = PageRequest.of(page, 10, sort);

        List<Product> filteredProducts = productRepository.findByCategoryIdsAndFilters(categories, minRating, maxRating, filterProperties);

        int start = (int) pageable.getOffset();
        int end = Math.min(start + pageable.getPageSize(), filteredProducts.size());

        return new PageImpl<>(filteredProducts.subList(start, end), pageable, filteredProducts.size());
    }

    @Override
    public Product getProduct(Long productId) {
        return productRepository.findById(productId)
                .orElseThrow(() -> new NoSuchElementException("Product not found with id: " + productId));
    }

    @Override
    public Integer checkProductsCount(Long productId) {
        return productRepository.findById(productId)
                .map(Product::getCount)
                .orElseThrow(() -> new NoSuchElementException("Product not found with id: " + productId));
    }

    @Override
    public List<Product> getProductsByIds(List<Long> ids) {
        return productRepository.findProductsByIds(ids);
    }

    @Transactional
    public void checkAndReduceProductQuantity(List<CellWithOutOrderDto> cells)
            throws Exception {
        for (CellWithOutOrderDto cell : cells) {
            Product product = productRepository.findById(cell.getProductId()).orElseThrow(
                    () -> new NoSuchElementException(String.format("Product not found with id %d ", cell.getProductId())));

            if (product.getCount() == 0) {
                throw new Exception(String.format(
                        "No product %s with an id %d in the warehouses",
                        product.getName(),
                        product.getId()));
            } else if (product.getCount() < cell.getCount()) {
                throw new Exception(String.format(
                        "Have only %d products %s out of %d",
                        product.getCount(),
                        product.getName(),
                        cell.getCount()));
            }

            product.setCount(product.getCount() - cell.getCount());
            productRepository.save(product);
        }
    }

    @Transactional
    @Override
    public Product createProduct(Product product) {
        return productRepository.save(product);
    }

    @Transactional
    @Override
    public Product updateProduct(Product product) {
        return productRepository.save(product);
    }

    @Transactional
    @Override
    public void deleteProduct(Long id) throws IOException {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Product not found"));

        List<String> filesToDelete = new ArrayList<>();

        if (product.getImagePath() != null) {
            filesToDelete.add(product.getImagePath());
        }

        List<ProductComment> comments = productCommentRepository.findByProductId(id);
        for (ProductComment comment : comments) {
            if (comment.getImagePath() != null) {
                filesToDelete.add(comment.getImagePath());
            }
        }

        imageService.deleteFiles(filesToDelete);
        productRepository.deleteById(id);
    }
}
