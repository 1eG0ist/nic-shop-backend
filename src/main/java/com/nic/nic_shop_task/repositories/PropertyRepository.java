package com.nic.nic_shop_task.repositories;

import com.nic.nic_shop_task.models.Property;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PropertyRepository extends CrudRepository<Property, Long> {
}
