package com.inventory.store.client.repository;

import com.inventory.store.client.entity.UserEntity;

import java.util.Optional;

public interface IUserRepository extends IQueryDslBaseRepository<UserEntity> {
    
    boolean existsByUsername(String username);

    Optional<UserEntity> findByUsernameAndActivo(String username, boolean b);
}
