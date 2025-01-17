package com.nic.nic_shop_task.repositories;

import com.nic.nic_shop_task.models.ProductProperties;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductPropertiesRepository extends CrudRepository<ProductProperties, Long> {
}
