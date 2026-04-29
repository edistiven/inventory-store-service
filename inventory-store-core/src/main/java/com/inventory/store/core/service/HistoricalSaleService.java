package com.inventory.store.core.service;

import com.inventory.store.client.entity.HistoricalSaleEntity;
import com.inventory.store.client.repository.IHistoricalSaleRepository;
import com.inventory.store.client.services.IHistoricalSaleService;
import com.inventory.store.core.constants.EntityConstants;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.UUID;

/**
 * Historical sale service implementation.
 *
 * @author eucsina on 24/04/2026
 */
@Slf4j
@Lazy
@Service
public class HistoricalSaleService extends GenericService<HistoricalSaleEntity, IHistoricalSaleRepository> implements IHistoricalSaleService {

    /**
     * Constructor.
     */
    public HistoricalSaleService(IHistoricalSaleRepository repository) {
        super(repository);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void saveHistoricalSale(Long itemCode, String itemName, Integer previousQuantity,
                                   Integer newQuantity, Integer quantitySold, String registeredBy) {

        HistoricalSaleEntity historical = HistoricalSaleEntity.builder()
                .uuid(UUID.randomUUID().toString())
                .itemCode(itemCode)
                .previousQuantity(previousQuantity)
                .newQuantity(newQuantity)
                .soldQuantity(quantitySold)
                .registeredBy(registeredBy)
                .registrationDate(new Date())
                .status(Boolean.TRUE)
                .version(EntityConstants.INITIAL)
                .build();

        repository.save(historical);
    }

}
