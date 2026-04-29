package com.inventory.store.client.services;

/**
 * Interface para el servicio de historial de ventas.
 *
 * @author eucsina on 24/04/2026
 */
public interface IHistoricalSaleService {

    /**
     * Guardar un registro en el historial de ventas
     */
    void saveHistoricalSale(Long itemCode, String itemName, Integer previousQuantity,
                            Integer newQuantity, Integer quantitySold, String registeredBy);
}
