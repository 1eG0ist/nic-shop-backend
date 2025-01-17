package com.nic.nic_shop_task.repositories;

import com.nic.nic_shop_task.models.OrderCell;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderCellRepository extends CrudRepository<OrderCell, Long> {
}
