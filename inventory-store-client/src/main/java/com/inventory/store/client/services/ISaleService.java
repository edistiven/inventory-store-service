package com.inventory.store.client.services;

import com.inventory.store.request.VentaMultipleVO;
import com.inventory.store.vo.HistoricalSalesResponseVO;
import com.inventory.store.vo.StockRequest;

/**
 * Interfaz para servicios de ventas
 */
public interface ISaleService {

    /**
     * Registrar una venta múltiple con varios productos
     *
     * @param ventaRequest VO con los detalles de la venta múltiple
     */
    void registrarVentaMultiple(VentaMultipleVO ventaRequest);

    /**
     * Obtener el histórico de ventas de un producto específico
     *
     * @param itemCode Código del producto
     * @return VO con el histórico completo del producto
     */
    HistoricalSalesResponseVO getHistoricalSalesItem(Long itemCode);

    /**
     * Agregar stock a un producto
     *
     * @param stockRequest VO con los detalles del agregado de stock
     */
    void addStockItem(StockRequest stockRequest);
}
