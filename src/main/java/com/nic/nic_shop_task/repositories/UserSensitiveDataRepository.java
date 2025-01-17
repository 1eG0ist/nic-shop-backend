package com.nic.nic_shop_task.repositories;

import com.nic.nic_shop_task.models.UserSensitiveData;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserSensitiveDataRepository extends CrudRepository<UserSensitiveData, Long> {
    Optional<UserSensitiveData> findByUserId(Long userId);
}
