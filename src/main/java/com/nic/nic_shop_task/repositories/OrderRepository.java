package com.nic.nic_shop_task.repositories;

import com.nic.nic_shop_task.models.Order;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderRepository extends CrudRepository<Order, Long> {
}
