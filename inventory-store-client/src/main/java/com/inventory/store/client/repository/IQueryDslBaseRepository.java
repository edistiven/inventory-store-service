package com.inventory.store.client.repository;

import java.io.Serializable;
import java.util.Collection;

public interface IQueryDslBaseRepository<T> {

    /**
     * Salvar una entidad.
     *
     * @param obj La entidad
     * @return La entidad guardada
     */
    void save(T obj);

    /**
     * Salvar una colleccion de entidad.
     *
     * @param objs Las entidades
     * @return Las entidades guardadas
     */
    void saveAll(Collection<T> objs);

    /**
     * Actualiza una entidad.
     *
     * @param obj La entidad
     * @return La entidad actualizada
     */
    void update(T obj);

    /**
     * Buscar por id una entidad.
     *
     * @param id El id
     * @return La entidad encontrada
     */
    T findById(Serializable id);

    /**
     * Obtener todas las entidades.
     *
     * @return Lista de todas las entidades
     */
    //List<T> findAll();

    /**
     * Eliminar una entidad por id.
     *
     * @param id El id
     */
    //void deleteById(ID id);

    /**
     * Eliminar una entidad.
     *
     * @param entity La entidad
     */
    //void delete(T entity);

    /**
     * Verificar si existe una entidad por id.
     *
     * @param id El id
     * @return true si existe, false si no
     */
    //boolean existsById(ID id);

    /**
     * Contar todas las entidades.
     *
     * @return Número total de entidades
     */
    //long count();
}
