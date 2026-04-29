package com.inventory.store.core.service;

import com.inventory.store.client.entity.CatalogEntity;
import com.inventory.store.client.repository.ICatalogRepository;
import com.inventory.store.client.services.ICatalogService;
import com.inventory.store.core.component.UserContextService;
import com.inventory.store.request.CatalogRequestVO;
import com.inventory.store.vo.CatalogVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

/**
 * Catalog service implementation.
 */
@Lazy
@Service
@Slf4j
public class CatalogService extends GenericService<CatalogEntity, ICatalogRepository> implements ICatalogService {

    @Autowired
    @Lazy
    private UserContextService userContextService;

    /**
     * Constructor.
     */
    public CatalogService(ICatalogRepository repository) {
        super(repository);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Page<CatalogVO> getCatalogsPaginated(CatalogRequestVO catalogRequest) {
        return repository.getCatalogsPaginated(catalogRequest);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void saveCatalog(CatalogVO catalogVO) {
        // Create CatalogEntity to save in database
        CatalogEntity newCatalogEntity = CatalogEntity.builder()
                .name(catalogVO.getName())
                .description(catalogVO.getDescription())
                .status(catalogVO.getStatus() != null ? catalogVO.getStatus() : true)
                .version(1)
                .registerUserDate(userContextService.getCurrentUser()) // Use current user
                .registerDate(new Date()) // Use current date
                .build();

        // Save catalog in database
        repository.save(newCatalogEntity);
        log.info("Catalog created successfully: {}", newCatalogEntity.getName());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void updateCatalog(CatalogVO catalogVO) {
        CatalogEntity catalogEntity = repository.findById(catalogVO.getId());

        if (catalogEntity == null) {
            throw new RuntimeException("Catalog not found with ID: " + catalogVO.getId());
        }

        // Update fields
        catalogEntity.setName(catalogVO.getName());
        catalogEntity.setDescription(catalogVO.getDescription());
        catalogEntity.setStatus(catalogVO.getStatus());

        // Get user performing the update
        String currentUser = userContextService.getCurrentUser();
        catalogEntity.setModificationUserDate(currentUser);
        catalogEntity.setModificationDate(new Date());

        // Save in database
        repository.update(catalogEntity);
        log.info("Catalog updated successfully: {}", catalogVO.getName());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public CatalogVO getCatalogById(Long id) {
        return repository.getCatalogById(id);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<CatalogVO> getActiveCatalogues() {
        return repository.getActiveCatalogs();
    }

}
