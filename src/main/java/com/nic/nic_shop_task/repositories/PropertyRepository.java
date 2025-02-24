package com.nic.nic_shop_task.repositories;

import com.nic.nic_shop_task.models.Property;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PropertyRepository extends CrudRepository<Property, Long> {
    Optional<Property> findByName(String name);
}
