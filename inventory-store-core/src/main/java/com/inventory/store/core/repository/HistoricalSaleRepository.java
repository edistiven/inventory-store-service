package com.inventory.store.core.repository;

import com.inventory.store.client.entity.HistoricalSaleEntity;
import com.inventory.store.client.entity.QHistoricalSaleEntity;
import com.inventory.store.client.repository.IHistoricalSaleRepository;
import com.inventory.store.vo.HistoricalSaleVO;
import com.querydsl.core.types.Projections;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

/**
 * Implementación de la interface IHistoricalSaleRepository.
 *
 * @author eucsina on 24/04/2026
 */
@Lazy
@Repository
public class HistoricalSaleRepository extends JPAQueryDsqlBaseRepository<HistoricalSaleEntity> implements IHistoricalSaleRepository {

    /**
     * Constructor.
     */
    public HistoricalSaleRepository() {
        super(HistoricalSaleEntity.class);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<HistoricalSaleVO> getHistoricalItems(Long itemCode) {
        QHistoricalSaleEntity historicalSaleEntity = QHistoricalSaleEntity.historicalSaleEntity;

        return from(historicalSaleEntity)
                .select(Projections.bean(HistoricalSaleVO.class,
                        historicalSaleEntity.uuid,
                        historicalSaleEntity.itemCode,
                        historicalSaleEntity.item.itemName.as("itemName"),
                        historicalSaleEntity.previousQuantity,
                        historicalSaleEntity.newQuantity,
                        historicalSaleEntity.soldQuantity,
                        historicalSaleEntity.registeredBy,
                        historicalSaleEntity.registrationDate))
                .where(historicalSaleEntity.itemCode.eq(itemCode)
                        .and(historicalSaleEntity.status.eq(true)))
                .orderBy(historicalSaleEntity.registrationDate.desc())
                .fetch();
    }

    @Override
    public List<HistoricalSaleVO> getHistorialPorUsuario(String registradoPor) {
        QHistoricalSaleEntity historialEntity = QHistoricalSaleEntity.historicalSaleEntity;

        return from(historialEntity)
                .select(com.querydsl.core.types.Projections.bean(HistoricalSaleVO.class,
                        historialEntity.uuid,
                        historialEntity.itemCode,
                        historialEntity.item.itemName.as("itemName"),
                        historialEntity.previousQuantity,
                        historialEntity.newQuantity,
                        historialEntity.soldQuantity,
                        historialEntity.registeredBy,
                        historialEntity.registrationDate))
                .where(historialEntity.registeredBy.eq(registradoPor))
                .orderBy(historialEntity.registrationDate.desc())
                .fetch();
    }

    @Override
    public List<HistoricalSaleVO> getHistorialPorRangoFechas(Date fechaInicio, Date fechaFin) {
        QHistoricalSaleEntity historialEntity = QHistoricalSaleEntity.historicalSaleEntity;

        return from(historialEntity)
                .select(com.querydsl.core.types.Projections.bean(HistoricalSaleVO.class,
                        historialEntity.uuid,
                        historialEntity.itemCode,
                        historialEntity.item.itemName.as("itemName"),
                        historialEntity.previousQuantity,
                        historialEntity.newQuantity,
                        historialEntity.soldQuantity,
                        historialEntity.registeredBy,
                        historialEntity.registrationDate))
                .where(historialEntity.registrationDate.between(fechaInicio, fechaFin))
                .orderBy(historialEntity.registrationDate.desc())
                .fetch();
    }

    @Override
    public List<HistoricalSaleVO> getHistorialPorItemYRangoFechas(Long itemCode, Date fechaInicio, Date fechaFin) {
        QHistoricalSaleEntity historialEntity = QHistoricalSaleEntity.historicalSaleEntity;

        return from(historialEntity)
                .select(com.querydsl.core.types.Projections.bean(HistoricalSaleVO.class,
                        historialEntity.uuid,
                        historialEntity.itemCode,
                        historialEntity.item.itemName.as("itemName"),
                        historialEntity.previousQuantity,
                        historialEntity.newQuantity,
                        historialEntity.soldQuantity,
                        historialEntity.registeredBy,
                        historialEntity.registrationDate))
                .where(historialEntity.itemCode.eq(itemCode)
                        .and(historialEntity.registrationDate.between(fechaInicio, fechaFin)))
                .orderBy(historialEntity.registrationDate.desc())
                .fetch();
    }
}
