package com.inventory.store.core.repository;

import com.inventory.store.client.entity.QUserEntity;
import com.inventory.store.client.entity.UserEntity;

import com.inventory.store.client.repository.IUserRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public class UserRepository extends JPAQueryDsqlBaseRepository<UserEntity> implements IUserRepository {

    /**
     * Constructor.
     */
    public UserRepository() {
        super(UserEntity.class);
    }

    @Override
    public Optional<UserEntity> findByUsernameAndActivo(String username, boolean activo) {
        QUserEntity userEntity = QUserEntity.userEntity;

        return Optional.ofNullable(from(userEntity)
                .where(userEntity.username.eq(username)
                        .and(userEntity.status.eq('1')))
                .fetchOne());
    }

    @Override
    public boolean existsByUsername(String username) {
        QUserEntity userEntity = QUserEntity.userEntity;

        return from(userEntity)
                .where(userEntity.username.eq(username))
                .fetchCount() > 0;
    }
}
