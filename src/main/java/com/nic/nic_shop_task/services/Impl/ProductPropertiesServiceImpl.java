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
    public ProductProperties createProductPropertyS(ProductPropertyDto productPropertyDto) throws RuntimeException {
        Property property = getOrCreateProperty(productPropertyDto);
        Optional<Product> product = productRepository.findById(productPropertyDto.getProductId());
        if (!product.isPresent()) {
            throw new RuntimeException("Product not found");
        }

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

        return productPropertiesRepository.save(productProperties);
    }

    @Override
    @Modifying
    public ProductProperties updateProductPropertyS(ProductProperties productProperties) {
        return productPropertiesRepository.save(productProperties);
    }

    @Override
    @Transactional
    @Modifying
    public void deleteProductPropertyS(Long id) throws RuntimeException {
        if (!productPropertiesRepository.existsById(id)) {
            throw new RuntimeException("No product properties with this id");
        }
        productPropertiesRepository.deleteById(id);
    }

    private Property getOrCreateProperty(ProductPropertyDto productPropertyDto) {
        if (productPropertyDto.getProperty().getId() == null) {
            return propertyRepository.save(productPropertyDto.getProperty());
        } else {
            return productPropertyDto.getProperty();
        }
    }
}
