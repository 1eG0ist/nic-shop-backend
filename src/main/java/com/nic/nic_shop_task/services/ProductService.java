package com.nic.nic_shop_task.services;

import com.nic.nic_shop_task.dtos.CellWithOutOrderDto;
import com.nic.nic_shop_task.dtos.FilterPropertyDto;
import com.nic.nic_shop_task.models.Product;
import org.springframework.data.domain.Page;

import java.io.IOException;
import java.util.List;
import java.util.NoSuchElementException;

public interface ProductService {
    Page<Product> getProducts(
            Long categoryId,
            String sort,
            Double minRating,
            Double maxRating,
            Integer page,
            List<FilterPropertyDto> filterProperties);
    Product getProduct(Long productId);
    Integer checkProductsCount(Long productId);
    void checkAndReduceProductQuantity(List<CellWithOutOrderDto> cells)
            throws Exception, NoSuchElementException, IllegalArgumentException;
    List<Product> getProductsByIds(List<Long> ids);
    Product createProduct(Product product);
    Product updateProduct(Product product);
    void deleteProduct(Long id) throws IOException;
}
