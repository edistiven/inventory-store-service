package com.inventory.store.core.service;

import com.inventory.store.client.repository.IQueryDslBaseRepository;
import org.springframework.transaction.annotation.Transactional;

import java.io.Serializable;

/**
 * Base services implementation with QUERYDSL support.
 * @param <T>
 * @param <R>
 */
@Transactional
public class GenericService<T, R extends IQueryDslBaseRepository<T>> implements IGenericService<T, R> {

    protected final R repository;

    /**
     * Constructor with dependencies.
     *
     * @param repository The repository to inject
     */
    public GenericService(R repository) {
        this.repository = repository;
    }

    @Override
    public void save(T t) {
        repository.save(t);
    }

    @Override
    public void update(T t) {
        repository.update(t);
    }

    @Override
    public T findById(Serializable id) {
        return repository.findById(id);
    }
}
