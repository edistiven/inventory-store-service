package com.inventory.store.client.services;

import com.inventory.store.request.CatalogRequestVO;
import com.inventory.store.vo.CatalogVO;
import org.springframework.data.domain.Page;

import java.util.List;

/**
 * Interface for catalog services.
 *
 * @author eucsina on 27/04/2026
 */
public interface ICatalogService {

    /**
     * Search catalogs with filters and pagination using VO.
     *
     * @param catalogRequest Request parameters
     * @return List of catalogs
     */
    Page<CatalogVO> getCatalogsPaginated(CatalogRequestVO catalogRequest);

    /**
     * Create a new catalog.
     *
     * @param catalogVO Catalog data to create
     */
    void saveCatalog(CatalogVO catalogVO);

    /**
     * Update an existing catalog using VO with update data.
     *
     * @param catalogVO VO with update data
     */
    void updateCatalog(CatalogVO catalogVO);

    /**
     * Get a catalog by its ID.
     *
     * @param id Catalog ID to search
     * @return CatalogVO found or null if not exists
     */
    CatalogVO getCatalogById(Long id);

    /**
     * Get all active catalogs.
     *
     * @return List of active catalogs
     */
    List<CatalogVO> getActiveCatalogues();

}
