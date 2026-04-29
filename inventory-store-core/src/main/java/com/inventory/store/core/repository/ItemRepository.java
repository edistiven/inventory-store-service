package com.inventory.store.core.repository;

import com.inventory.store.client.entity.ItemEntity;
import com.inventory.store.client.entity.QItemEntity;
import com.inventory.store.client.entity.QCatalogEntity;
import com.inventory.store.client.repository.IItemRepository;
import com.inventory.store.request.ItemRequestVO;
import com.inventory.store.vo.ItemVO;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Projections;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;
import org.apache.commons.lang3.StringUtils;
import java.util.List;

/**
 * Implementación de la interface IItemRepository.
 *
 * @author eucsina on 24/04/2026
 */
@Lazy
@Repository
public class ItemRepository extends JPAQueryDsqlBaseRepository<ItemEntity> implements IItemRepository {

    /**
     * Constructor.
     */
    public ItemRepository() {
        super(ItemEntity.class);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ItemVO getItemByItemCode(Long itemCode) {
        QItemEntity itemEntity = QItemEntity.itemEntity;
        QCatalogEntity catalogEntity = QCatalogEntity.catalogEntity;
        return from(itemEntity)
                .leftJoin(itemEntity.catalogEntity, catalogEntity)
                .select(Projections.bean(ItemVO.class, itemEntity.itemCode,
                        itemEntity.barCode,
                        itemEntity.itemName.as("name"), itemEntity.itemDescription.as("description"),
                        itemEntity.price, itemEntity.quantity,
                        itemEntity.expirationDate, itemEntity.status,
                        catalogEntity.id.as("catalogueId")))
                .where(itemEntity.itemCode.eq(itemCode))
                .fetchOne();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ItemEntity findById(Long itemCode) {
        QItemEntity itemEntity = QItemEntity.itemEntity;
        return from(itemEntity)
                .select(itemEntity)
                .where(itemEntity.itemCode.eq(itemCode))
                .fetchOne();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Page<ItemVO> getItemsPaginated(ItemRequestVO itemRequest) {
        QItemEntity itemEntity = QItemEntity.itemEntity;
        QCatalogEntity catalogEntity = QCatalogEntity.catalogEntity;

        // Construir consulta base con Projections.bean
        var query = from(itemEntity)
                .leftJoin(itemEntity.catalogEntity, catalogEntity)
                .select(Projections.bean(ItemVO.class, 
                        itemEntity.itemCode,
                        itemEntity.barCode,
                        itemEntity.itemName.as("name"), 
                        itemEntity.itemDescription.as("description"),
                        itemEntity.price, 
                        itemEntity.quantity,
                        itemEntity.expirationDate, 
                        itemEntity.soldQuantity.as("quantitySold"),
                        itemEntity.status,
                        catalogEntity.id.as("catalogueId"),
                        catalogEntity.name.as("catalogueName")));

        // Aplicar filtros si se proporcionan
        if (StringUtils.isNotEmpty(itemRequest.getBarCode())) {
            query = query.where(itemEntity.barCode.containsIgnoreCase(itemRequest.getBarCode()));
        }

        if (StringUtils.isNotEmpty(itemRequest.getName())) {
            query = query.where(itemEntity.itemName.containsIgnoreCase(itemRequest.getName()));
        }

        if (StringUtils.isNotEmpty(itemRequest.getDescription())) {
            query = query.where(itemEntity.itemDescription.containsIgnoreCase(itemRequest.getDescription()));
        }

        if(itemRequest.getStatus() != null){
            query = query.where(itemEntity.status.eq(itemRequest.getStatus()));
        }

        query.orderBy(itemEntity.barCode.asc());

        // Obtener el total de elementos para la paginación
        long totalElements = query.fetchCount();

        // Aplicar paginación
        Pageable pageable = PageRequest.of(itemRequest.getPage(), itemRequest.getSize());

        List<ItemVO> items = query
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        return new PageImpl<>(items, pageable, totalElements);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<ItemVO> getActiveItems(String name) {
        QItemEntity itemEntity = QItemEntity.itemEntity;

        BooleanBuilder predicate = new BooleanBuilder();
        predicate.and(itemEntity.status.eq(true));
        if(StringUtils.isNotBlank(name)){
            predicate.and(itemEntity.itemName.containsIgnoreCase(name.toUpperCase()));
        }

        return from(itemEntity)
                .select(Projections.bean(ItemVO.class,
                        itemEntity.itemCode,
                        itemEntity.barCode,
                        itemEntity.itemName.as("name"),
                        itemEntity.itemDescription.as("description"),
                        itemEntity.price,
                        itemEntity.quantity,
                        itemEntity.expirationDate,
                        itemEntity.status))
                .where(predicate)
                .fetch();
    }

}
