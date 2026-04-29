package com.inventory.store.services.controller.item;

import com.inventory.store.client.services.IItemService;
import com.inventory.store.request.ItemRequestVO;
import com.inventory.store.services.controller.base.ApiResponse;
import com.inventory.store.vo.EstadisticasInventarioVO;
import com.inventory.store.vo.ItemVO;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/inventario")
@RequiredArgsConstructor
@Slf4j
public class ItemController {

    @Autowired
    @Lazy
    private final IItemService itemService;

    @PostMapping("/getItemsPaginated")
    public ResponseEntity<ApiResponse<Page<ItemVO>>> getItemsPaginated(@Valid @RequestBody ItemRequestVO requestVO) {
        try {
            log.info("Buscando productos con filtros: {}", requestVO);

            // Llamar al servicio con el VO de búsqueda
            Page<ItemVO> response = itemService.getItemsPaginated(requestVO);

            if (CollectionUtils.isEmpty(response.getContent())) {
                return ResponseEntity.ok(ApiResponse.success(response, "No se encontraron productos con los filtros seleccionados"));
            }

            return ResponseEntity.ok(ApiResponse.success(response, "Productos encontrados exitosamente"));

        } catch (Exception e) {
            log.error("Error al buscar productos con filtros: {}", requestVO, e);
            return ResponseEntity.internalServerError()
                    .body(ApiResponse.error("Error al buscar productos: " + e.getMessage()));
        }
    }

    /**
     * Create a new item.
     *
     * @param itemVO Item data to create
     * @return Created item
     */
    @PostMapping("/saveItem")
    public ResponseEntity<ApiResponse<Void>> createItem(@Valid @RequestBody ItemVO itemVO) {
        try {
            itemService.saveItem(itemVO);
            return ResponseEntity.ok(ApiResponse.success(null));
        } catch (Exception e) {
            log.error("Error al crear item", e);
            return ResponseEntity.badRequest()
                    .body(ApiResponse.error("Error al crear el articulo: " + e.getMessage()));
        }
    }

    /**
     * Update an existing item.
     *
     * @param itemVO Item data to update
     * @return Updated item
     */
    @PostMapping("/updateItem")
    public ResponseEntity<ApiResponse<String>> updateItem(@Valid @RequestBody ItemVO itemVO) {
        try {
            itemService.updateItem(itemVO);
            return ResponseEntity.ok(ApiResponse.success("Item actualizado exitosamente"));
        } catch (Exception e) {
            log.error("Error al actualizar producto", e);
            return ResponseEntity.internalServerError()
                    .body(ApiResponse.error("Error al actualizar producto: " + e.getMessage()));
        }
    }

    /**
     * Get an item by its item code.
     *
     * @param itemCode Item code to retrieve
     * @return Item data
     */
    @GetMapping("/getItem/{itemCode}")
    public ResponseEntity<ApiResponse<ItemVO>> getItem(@PathVariable Long itemCode) {
        try {
            ItemVO item = itemService.getItemByItemCode(itemCode);
            if (item == null) {
                return ResponseEntity.ok(ApiResponse.error("Producto no encontrado"));
            }
            return ResponseEntity.ok(ApiResponse.success(item));
        } catch (Exception e) {
            log.error("Error al obtener producto por itemCode: {}", itemCode, e);
            return ResponseEntity.internalServerError()
                    .body(ApiResponse.error("Error al obtener producto"));
        }
    }

    /**
     * Delete an item by its item code.
     *
     * @param itemCode Item code to delete
     * @return Delete confirmation message
     */
    @PostMapping("/deleteItem/{itemCode}")
    public ResponseEntity<ApiResponse<String>> deleteItem(@PathVariable Long itemCode) {
        try {
            boolean nuevoEstado = itemService.deleteItem(itemCode);
            String mensaje = nuevoEstado ? "Producto activado exitosamente" : "Producto desactivado exitosamente";
            return ResponseEntity.ok(ApiResponse.success(mensaje));
        } catch (Exception e) {
            log.error("Error al cambiar estado del producto con itemCode: {}", itemCode, e);
            return ResponseEntity.internalServerError()
                    .body(ApiResponse.error("Error al cambiar estado del producto"));
        }
    }

    /**
     * Get a list of active items.
     *
     * @param name Name of the item to search for
     * @return List of active items
     */
    @GetMapping("/getActiveItems")
    public ResponseEntity<ApiResponse<List<ItemVO>>> getActiveItems(
            @RequestParam String name) {
        try {
            log.info("Buscando productos activos por nombre: {}", name);

            List<ItemVO> productosActivos = itemService.getActiveItems(name);

            if (productosActivos.isEmpty()) {
                log.info("No se encontraron productos activos con nombre que contenga: {}", name);
                return ResponseEntity.ok(ApiResponse.success(productosActivos,
                        "No se encontraron productos activos con el criterio de búsqueda"));
            }

            log.info("Se encontraron {} productos activos", productosActivos.size());
            return ResponseEntity.ok(ApiResponse.success(productosActivos));

        } catch (Exception e) {
            log.error("Error al buscar productos activos por nombre: {}", name, e);
            return ResponseEntity.internalServerError()
                    .body(ApiResponse.error("Error al buscar productos activos"));
        }
    }

    /**
     * Get inventory statistics.
     *
     * @return Inventory statistics
     */
    @GetMapping("/estadisticas")
    public ResponseEntity<ApiResponse<EstadisticasInventarioVO>> obtenerEstadisticasInventario() {
        try {
            log.info("Obteniendo estadísticas del inventario");

            EstadisticasInventarioVO estadisticas = itemService.getInventoryStatistics();

            return ResponseEntity.ok(ApiResponse.success(estadisticas, "Estadísticas obtenidas exitosamente"));

        } catch (Exception e) {
            log.error("Error al obtener estadísticas del inventario", e);
            return ResponseEntity.internalServerError()
                    .body(ApiResponse.error("Error al obtener estadísticas del inventario: " + e.getMessage()));
        }
    }
}
