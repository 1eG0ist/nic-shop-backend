package com.nic.nic_shop_task.repositories;

import com.nic.nic_shop_task.models.User;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.Optional;

@Repository
public interface UserRepository extends CrudRepository<User, Long> {
    Optional<User> findUserByEmail(String email);
    Optional<User> findUserById(Long id);

    @Transactional
    @Modifying
    @Query(
            value = "insert into user_roles values " +
                    "(:userId, (select id from roles where name = 'ROLE_ADMIN'))",
    nativeQuery = true)
    void addAdminRoleToUser(Long userId);
}
