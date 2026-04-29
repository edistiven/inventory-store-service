package com.inventory.store.core.repository;

import com.inventory.store.client.entity.CatalogEntity;
import com.inventory.store.client.entity.QCatalogEntity;
import com.inventory.store.client.repository.ICatalogRepository;
import com.inventory.store.request.CatalogRequestVO;
import com.inventory.store.vo.CatalogVO;
import com.querydsl.core.types.Projections;
import org.apache.commons.lang3.StringUtils;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import com.querydsl.core.Tuple;

/**
 * Implementation of ICatalogRepository interface.
 *
 * @author eucsina on 27/04/2026
 */
@Lazy
@Repository
public class CatalogRepository extends JPAQueryDsqlBaseRepository<CatalogEntity> implements ICatalogRepository {

    /**
     * Constructor.
     */
    public CatalogRepository() {
        super(CatalogEntity.class);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Page<CatalogVO> getCatalogsPaginated(CatalogRequestVO catalogRequest) {
        System.out.println("catalogRequest = " + catalogRequest);
        QCatalogEntity catalogEntity = QCatalogEntity.catalogEntity;

        // Construir consulta base con Projections.bean
        var query = from(catalogEntity)
                .select(Projections.bean(CatalogVO.class, 
                        catalogEntity.id,
                        catalogEntity.name,
                        catalogEntity.description,
                        catalogEntity.status,
                        catalogEntity.version,
                        catalogEntity.registerDate,
                        catalogEntity.registerUserDate,
                        catalogEntity.modificationDate,
                        catalogEntity.modificationUserDate));

        if (StringUtils.isNotEmpty(catalogRequest.getName())) {
            query = query.where(catalogEntity.name.containsIgnoreCase(catalogRequest.getName()));
        }

        if (StringUtils.isNotEmpty(catalogRequest.getDescription())) {
            query = query.where(catalogEntity.description.containsIgnoreCase(catalogRequest.getDescription()));
        }

        query.orderBy(catalogEntity.name.asc());

        // Obtener el total de elementos para la paginación
        long totalElements = query.fetchCount();

        // Aplicar paginación
        Pageable pageable = PageRequest.of(catalogRequest.getPage(), catalogRequest.getSize());

        List<CatalogVO> catalogs = query
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();
        return new PageImpl<>(catalogs, pageable, totalElements);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public CatalogVO getCatalogById(Long id) {
        CatalogEntity catalogEntity = findById(id);
        if (catalogEntity != null) {
            return convertToVO(catalogEntity);
        }
        return null;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public CatalogEntity findById(Long id) {
        return getEntityManager().find(CatalogEntity.class, id);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<CatalogVO> getActiveCatalogs() {
        QCatalogEntity catalogEntity = QCatalogEntity.catalogEntity;

        var query = from(catalogEntity)
                .select(Projections.bean(CatalogVO.class,
                        catalogEntity.id,
                        catalogEntity.name,
                        catalogEntity.description,
                        catalogEntity.status,
                        catalogEntity.version,
                        catalogEntity.registerDate,
                        catalogEntity.registerUserDate,
                        catalogEntity.modificationDate,
                        catalogEntity.modificationUserDate))
                .where(catalogEntity.status.eq(true));

        return query.fetch();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Map<String, Long> getAllActiveCatalogsMap() {
        QCatalogEntity catalogEntity = QCatalogEntity.catalogEntity;

        var query = from(catalogEntity)
                .select(catalogEntity.id, catalogEntity.name)
                .where(catalogEntity.status.eq(true));

        List<Tuple> results = query.fetch();
        
        Map<String, Long> catalogMap = new ConcurrentHashMap<>();
        for (Tuple tuple : results) {
            Long id = tuple.get(catalogEntity.id);
            String name = tuple.get(catalogEntity.name);
            catalogMap.put(name.toUpperCase(), id); // Guardar en mayúsculas para consistencia
        }

        return catalogMap;
    }


    private CatalogVO convertToVO(CatalogEntity entity) {
        if (entity == null) {
            return null;
        }

        return CatalogVO.builder()
                .id(entity.getId())
                .name(entity.getName())
                .description(entity.getDescription())
                .status(entity.getStatus())
                .registerDate(entity.getRegisterDate())
                .registerUserDate(entity.getRegisterUserDate())
                .modificationDate(entity.getModificationDate())
                .modificationUserDate(entity.getModificationUserDate())
                .build();
    }
}
