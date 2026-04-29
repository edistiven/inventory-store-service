package com.inventory.store.client.repository;

import com.inventory.store.client.entity.CatalogEntity;
import com.inventory.store.request.CatalogRequestVO;
import com.inventory.store.vo.CatalogVO;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.Map;

/**
 * Interface for catalog repository.
 *
 * @author eucsina on 27/04/2026
 */
public interface ICatalogRepository extends IQueryDslBaseRepository<CatalogEntity> {

    /**
     * Get catalogs with pagination and filters.
     *
     * @param catalogRequest Request parameters
     * @return List of catalogs
     */
    Page<CatalogVO> getCatalogsPaginated(CatalogRequestVO catalogRequest);

    /**
     * Get catalog by ID.
     *
     * @param id Catalog ID
     * @return CatalogVO found or null
     */
    CatalogVO getCatalogById(Long id);

    /**
     * Find catalog entity by ID.
     *
     * @param id Catalog ID
     * @return CatalogEntity found or null
     */
    CatalogEntity findById(Long id);

    /**
     * Get active catalogs by name.
     *
     * @return List of active catalogs
     */
    List<CatalogVO> getActiveCatalogs();

    /**
     * Get all active catalogs as Map with name as key and ID as value.
     *
     * @return Map of catalog name -> catalog ID
     */
    Map<String, Long> getAllActiveCatalogsMap();

}
