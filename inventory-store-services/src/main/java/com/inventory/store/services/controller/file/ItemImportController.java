package com.inventory.store.services.controller.file;

import com.inventory.store.client.entity.ItemEntity;
import com.inventory.store.client.service.ExcelItemImportService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Controller for importing items from Excel files
 *
 * @author eucsina on 28/04/2026
 */
@RestController
@RequestMapping("/api/import")
@RequiredArgsConstructor
@Slf4j
public class ItemImportController {

    @Autowired
    @Lazy
    private final ExcelItemImportService excelItemImportService;

    /**
     * Import items from Excel file by path
     *
     * @param filePath Path to the Excel file
     * @return Response with import results
     */
    @GetMapping("/items/excel/path")
    public ResponseEntity<Map<String, Object>> importItemsFromExcelPath(@RequestParam String filePath) {
        log.info("Starting Excel import process for file path: {}", filePath);

        Map<String, Object> response = new ConcurrentHashMap<>();

        try {
            // Importar items desde el archivo Excel usando la ruta
            List<ItemEntity> importedItems = excelItemImportService.importItemsFromExcelPath(filePath);

            // Preparar respuesta exitosa
            response.put("success", true);
            response.put("message", "Importación completada exitosamente");
            response.put("totalItems", importedItems.size());
            response.put("items", importedItems);

            log.info("Excel import completed successfully. {} items imported", importedItems.size());
            return ResponseEntity.ok(response);

        } catch (Exception e) {
            log.error("Error during Excel import: {}", e.getMessage(), e);

            // Preparar respuesta de error
            response.put("success", false);
            response.put("message", "Error durante la importación: " + e.getMessage());
            response.put("totalItems", 0);
            response.put("items", null);

            return ResponseEntity.badRequest().body(response);
        }
    }
}
