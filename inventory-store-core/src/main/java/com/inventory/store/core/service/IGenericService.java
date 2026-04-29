package com.inventory.store.core.service;

import java.io.Serializable;

public interface IGenericService<T, R> {

    /**
     * Save entity
     *
     * @param t The entity
     */
    void save(T t);

    /**
     * Update entity
     *
     * @param t The entity
     */
    void update(T t);

    /**
     * @param id The id entity
     * @return The entity <T>
     */
    T findById(Serializable id);
}
