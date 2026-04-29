package com.inventory.store.client.services;

import com.inventory.store.request.ItemRequestVO;
import com.inventory.store.vo.EstadisticasInventarioVO;
import com.inventory.store.vo.ItemVO;
import org.springframework.data.domain.Page;

import java.util.List;

/**
 * Interface for product services.
 */
public interface IItemService {

    /**
     * Buscar productos con filtros y paginación retornando datos y total.
     *
     * @param busqueda VO con parámetros de búsqueda
     * @return PaginatedResponse con datos y total de registros
     */
    Page<ItemVO> getItemsPaginated(ItemRequestVO busqueda);

    /**
     * Create a new item.
     *
     * @param itemVO
     */
    void saveItem(ItemVO itemVO);

    /**
     * Actualizar un item existente usando VO con datos de actualización.
     *
     * @param itemVO VO con datos de actualización
     */
    void updateItem(ItemVO itemVO);

    /**
     * Obtener un item por su itemCode.
     *
     * @param itemCode Código del item a buscar
     * @return ItemVO encontrado o null si no existe
     */
    ItemVO getItemByItemCode(Long itemCode);

    /**
     * Cambiar el estado de un producto (activar/desactivar).
     *
     * @param itemCode Código del item a cambiar estado
     * @return Nuevo estado del producto (true = activo, false = inactivo)
     */
    boolean deleteItem(Long itemCode);

    /**
     * Buscar productos activos por nombre.
     *
     * @param nombre Nombre del producto a buscar (en mayúsculas)
     * @return Lista de productos activos que coinciden con el nombre
     */
    List<ItemVO> getActiveItems(String name);

    /**
     * Obtener estadísticas completas del inventario.
     *
     * @return EstadisticasInventarioVO con datos calculados del inventario
     */
    EstadisticasInventarioVO getInventoryStatistics();

}
