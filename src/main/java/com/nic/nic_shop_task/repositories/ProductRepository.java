package com.nic.nic_shop_task.repositories;

import com.nic.nic_shop_task.models.Product;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductRepository extends CrudRepository<Product, Long> {
}
