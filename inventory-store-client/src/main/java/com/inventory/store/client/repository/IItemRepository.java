package com.inventory.store.client.repository;

import com.inventory.store.client.entity.ItemEntity;
import com.inventory.store.request.ItemRequestVO;
import com.inventory.store.vo.ItemVO;
import org.springframework.data.domain.Page;

import java.util.List;

/**
 * Interface para el repositorio de items.
 *
 * @author eucsina on 24/04/2026
 */
public interface IItemRepository extends IQueryDslBaseRepository<ItemEntity> {

    /**
     * Buscar productos con filtros y paginación desde base de datos.
     */
    Page<ItemVO> getItemsPaginated(ItemRequestVO itemRequest);

    /**
     * Buscar un item por su itemCode.
     *
     * @param itemCode
     * @return
     */
    ItemVO getItemByItemCode(Long itemCode);

    /**
     * Buscar un item por su itemCode.
     *
     * @param itemCode
     * @return
     */
    ItemEntity findById(Long itemCode);

    /**
     * Buscar productos activos por nombre.
     *
     * @param name Nombre del producto a buscar (en mayúsculas)
     * @return Lista de productos activos que coinciden con el nombre
     */
    List<ItemVO> getActiveItems(String name);


}
