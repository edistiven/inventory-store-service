package com.inventory.store.client.repository;

import com.inventory.store.client.entity.HistoricalSaleEntity;
import com.inventory.store.vo.HistoricalSaleVO;

import java.util.Date;
import java.util.List;


/**
 * Interface para el repositorio de historial de ventas.
 *
 * @author eucsina on 24/04/2026
 */
public interface IHistoricalSaleRepository extends IQueryDslBaseRepository<HistoricalSaleEntity> {

    /**
     * Buscar historial de ventas por código de item
     */
    List<HistoricalSaleVO> getHistoricalItems(Long itemCode);

    /**
     * Buscar historial de ventas por usuario
     */
    List<HistoricalSaleVO> getHistorialPorUsuario(String registradoPor);

    /**
     * Buscar historial de ventas por rango de fechas
     */
    List<HistoricalSaleVO> getHistorialPorRangoFechas(Date fechaInicio, Date fechaFin);

    /**
     * Buscar historial de ventas por item y rango de fechas
     */
    List<HistoricalSaleVO> getHistorialPorItemYRangoFechas(Long itemCode, Date fechaInicio, Date fechaFin);

}
