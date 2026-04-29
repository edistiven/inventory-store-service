package com.inventory.store.services.controller.catalog;

import com.inventory.store.client.services.ICatalogService;
import com.inventory.store.request.CatalogRequestVO;
import com.inventory.store.services.controller.base.ApiResponse;
import com.inventory.store.vo.CatalogVO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;

import java.util.List;

/**
 * REST Controller for Catalog management.
 *
 * @author eucsina on 27/04/2026
 */
@RestController
@RequestMapping("/api/catalogos")
@RequiredArgsConstructor
@Slf4j
public class CatalogController {

    private final ICatalogService catalogService;

    /**
     * Get catalogs with pagination and filters.
     *
     * @param catalogRequest Request parameters
     * @return List of catalogs
     */
    @PostMapping("/getCatalogsPaginated")
    public ResponseEntity<ApiResponse<Page<CatalogVO>>> getCatalogsPaginated(@RequestBody CatalogRequestVO catalogRequest) {
        try {
            Page<CatalogVO> response = catalogService.getCatalogsPaginated(catalogRequest);
            if (CollectionUtils.isEmpty(response.getContent())) {
                return ResponseEntity.ok(ApiResponse.success(response, "No se encontraron catalogos con los filtros seleccionados."));
            }

            return ResponseEntity.ok(ApiResponse.success(response, "Catalogos encontrados exitosamente."));

        } catch (Exception e) {
            log.error("Error getting catalogs: {}", e.getMessage());
            return ResponseEntity.internalServerError()
                    .body(ApiResponse.error("Error al buscar los catalogs: " + e.getMessage()));
        }
    }

    /**
     * Create a new catalog.
     *
     * @param catalogVO Catalog data to create
     * @return Created catalog
     */
    @PostMapping("/saveCatalog")
    public ResponseEntity<ApiResponse<Void>> createCatalog(@Valid @RequestBody CatalogVO catalogVO) {
        try {
            catalogService.saveCatalog(catalogVO);
            return ResponseEntity.ok(ApiResponse.success(null));
        } catch (Exception e) {
            log.error("Error al crear item", e);
            return ResponseEntity.badRequest()
                    .body(ApiResponse.error("Error al crear el catálogo: " + e.getMessage()));
        }
    }

    /**
     * Get catalog by ID.
     *
     * @param id Catalog ID
     * @return ApiResponse with catalog found
     */
    @GetMapping("/getCatalogueById/{id}")
    public ResponseEntity<ApiResponse<CatalogVO>> getCatalogById(@PathVariable Long id) {
        try {
            CatalogVO catalog = catalogService.getCatalogById(id);
            if (catalog != null) {
                ApiResponse<CatalogVO> response = new ApiResponse<>(
                        true,
                        "Catálogo encontrado exitosamente",
                        catalog,
                        null
                );
                return ResponseEntity.ok(response);
            } else {
                ApiResponse<CatalogVO> response = new ApiResponse<>(
                        false,
                        "Catálogo no encontrado",
                        null,
                        "Catálogo con ID " + id + " no existe"
                );
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
            }
        } catch (Exception e) {
            log.error("Error getting catalog by ID {}: {}", id, e.getMessage());
            ApiResponse<CatalogVO> response = new ApiResponse<>(
                    false,
                    "Error al obtener catálogo",
                    null,
                    e.getMessage()
            );
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    /**
     * Update an existing catalog.
     *
     * @param catalogVO Updated catalog data
     * @return Updated catalog
     */
    @PostMapping("/updateCatalogue")
    public ResponseEntity<ApiResponse<String>> updateCatalog(@Valid @RequestBody CatalogVO catalogVO) {

        try {
            log.info("Updating catalog ID: {}", catalogVO.getId());
            catalogService.updateCatalog(catalogVO);
            return ResponseEntity.ok(ApiResponse.success("Catalogo actualizado exitosamente"));
        } catch (Exception e) {
            log.error("Error updating catalog {}: {}", catalogVO.getId(), e.getMessage());
            return ResponseEntity.internalServerError()
                    .body(ApiResponse.error("Error al actualizar el catalogo: " + e.getMessage()));
        }
    }

    /**
     * Search catalogs by name.
     *
     * @return List of matching catalogs
     */
    @GetMapping("/getActiveCatalogues")
    public ResponseEntity<ApiResponse<List<CatalogVO>>> getActiveCatalogues() {
        try {
            List<CatalogVO> catalogs = catalogService.getActiveCatalogues();
            if (CollectionUtils.isEmpty(catalogs)) {
                return ResponseEntity.ok(ApiResponse.success(null, "No se encontraron catálogos activos"));
            }
            return ResponseEntity.ok(ApiResponse.success(catalogs, "Catálogos activos obtenidos exitosamente"));
        } catch (Exception e) {
            log.error("Error searching catalogs: {}", e.getMessage());
            return ResponseEntity.ok(ApiResponse.error("Error al obtener los catálogos activos"));
        }
    }
}
