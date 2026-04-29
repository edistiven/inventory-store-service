package com.inventory.store.core.repository;

import com.inventory.store.client.repository.IQueryDslBaseRepository;
import com.querydsl.core.types.Path;
import com.querydsl.jpa.JPQLQuery;
import com.querydsl.jpa.impl.JPAQuery;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;

import java.io.Serializable;
import java.util.Collection;

/**
 * Repository base para JPA y QueryDSL.
 * @param <T>
 */
public abstract class JPAQueryDsqlBaseRepository<T> extends QuerydslRepositorySupport
        implements IQueryDslBaseRepository<T> {

    private final Class<T> domainClass;

    @PersistenceContext
    protected EntityManager entityManager;

    public JPAQueryDsqlBaseRepository(Class<T> domainClass) {
        super(domainClass);
        this.domainClass = domainClass;
    }

    @Override
    public void save(T obj) {
        if (obj == null) {
            throw new IllegalArgumentException("Entity cannot be null");
        }
        entityManager.persist(obj);
    }

    @Override
    public void saveAll(Collection<T> objs) {
        objs.forEach(obj -> getEntityManager().persist(obj));
    }


    @Override
    public void update(T obj) {
        getEntityManager().merge(obj);
    }

    @Override
    public T findById(Serializable id) {
        return getEntityManager().find(domainClass, id);
    }

}
