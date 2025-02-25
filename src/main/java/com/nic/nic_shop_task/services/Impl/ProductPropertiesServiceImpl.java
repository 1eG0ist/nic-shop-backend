package com.nic.nic_shop_task.services.Impl;

import com.nic.nic_shop_task.dtos.ProductPropertyDto;
import com.nic.nic_shop_task.models.Product;
import com.nic.nic_shop_task.models.ProductProperties;
import com.nic.nic_shop_task.models.Property;
import com.nic.nic_shop_task.repositories.ProductPropertiesRepository;
import com.nic.nic_shop_task.repositories.ProductRepository;
import com.nic.nic_shop_task.repositories.PropertyRepository;
import com.nic.nic_shop_task.services.ProductPropertiesService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ProductPropertiesServiceImpl implements ProductPropertiesService {
    private final ProductPropertiesRepository productPropertiesRepository;
    private final PropertyRepository propertyRepository;
    private final ProductRepository productRepository;
    @Override
    @Transactional
    public ResponseEntity<?> createProductPropertyS(ProductPropertyDto productPropertyDto) {
        Property property;
        if (productPropertyDto.getProperty().getId() == null) {
            property = propertyRepository.save(productPropertyDto.getProperty());
        } else {
            property = productPropertyDto.getProperty();
        }

        Optional<Product> product = productRepository.findById(productPropertyDto.getProductId());
        if (!product.isPresent()) return ResponseEntity.status(HttpStatus.NOT_FOUND).build();

        ProductProperties productProperties = new ProductProperties(
                null,
                product.get(),
                property,
                productPropertyDto.getNumericValue(),
                productPropertyDto.getRangeStartValue(),
                productPropertyDto.getRangeEndValue(),
                productPropertyDto.getIsValue(),
                productPropertyDto.getTextValue()
        );

        return ResponseEntity.ok(productPropertiesRepository.save(productProperties));
    }

    @Override
    public ResponseEntity<?> updateProductPropertyS(ProductProperties productProperties) {
        productPropertiesRepository.save(productProperties);
        return ResponseEntity.ok().build();
    }

    @Override
    @Transactional
    @Modifying
    public ResponseEntity<?> deleteProductPropertyS(Long id) {
        productPropertiesRepository.deleteById(id);
        return ResponseEntity.ok().build();
    }
}
