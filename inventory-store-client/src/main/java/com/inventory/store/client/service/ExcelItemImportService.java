package com.inventory.store.client.service;

import com.inventory.store.client.entity.ItemEntity;
import com.inventory.store.client.repository.IItemRepository;
import com.inventory.store.client.repository.ICatalogRepository;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.List;
import java.util.Map;
import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Random;

/**
 * Service for importing items from Excel files to INV_ITEM table
 *
 * @author eucsina on 28/04/2026
 */
@Service
@Slf4j
public class ExcelItemImportService {

    @Autowired
    @Lazy
    private IItemRepository itemRepository;

    @Autowired
    @Lazy
    private ICatalogRepository catalogRepository;

    private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd");

    /**
     * Import items from Excel file by file path
     *
     * @param filePath Path to the Excel file
     * @return List of imported ItemEntity objects
     * @throws IOException if file cannot be read
     */
    @Transactional
    public List<ItemEntity> importItemsFromExcelPath(String filePath) throws IOException {
        log.info("Starting import from Excel file path: {}", filePath);

        // Normalizar la ruta: convertir backslashes a forward slashes y decodificar URL encoding
        String normalizedPath = normalizeFilePath(filePath);
        log.debug("Normalized file path: {}", normalizedPath);

        File file = new File(normalizedPath);
        if (!file.exists()) {
            throw new RuntimeException("El archivo no existe: " + filePath);
        }

        if (!file.getName().toLowerCase().endsWith(".xlsx")) {
            throw new RuntimeException("El archivo debe estar en formato Excel (.xlsx)");
        }

        List<ItemEntity> importedItems = new ArrayList<>();

        // Obtener el mapa de catálogos activos
        Map<String, Long> catalogMap = catalogRepository.getAllActiveCatalogsMap();

        try (FileInputStream fis = new FileInputStream(file);
             Workbook workbook = new XSSFWorkbook(fis)) {
            Sheet sheet = workbook.getSheetAt(0);

            // Procesar filas de datos (empezando desde la fila 0, sin encabezados)
            for (int i = 0; i <= sheet.getLastRowNum(); i++) {
                Row row = sheet.getRow(i);

                if (row == null || isEmptyRow(row)) {
                    continue; // Saltar filas vacías
                }

                try {
                    ItemEntity item = mapRowToItemEntity(row, catalogMap);
                    itemRepository.save(item);
                    importedItems.add(item);

                    log.debug("Item imported successfully: {}", item.getItemName());
                } catch (Exception e) {
                    log.error("Error importing item from row {}: {}", i + 1, e.getMessage());
                    // Continuar con la siguiente fila
                }
            }
        }

        log.info("Import completed. {} items imported successfully", importedItems.size());
        return importedItems;
    }

    private boolean isEmptyRow(Row row) {
        for (int i = 0; i < row.getLastCellNum(); i++) {
            Cell cell = row.getCell(i);
            if (cell != null && cell.getCellType() != CellType.BLANK) {
                return false;
            }
        }
        return true;
    }

    private ItemEntity mapRowToItemEntity(Row row, Map<String, Long> catalogMap) throws ParseException {
        ItemEntity itemEntity = new ItemEntity();

        // Columna 0: Nombre del Articulo (requerido)
        Cell nameCell = row.getCell(0);
        if (nameCell == null || nameCell.getCellType() == CellType.BLANK) {
            throw new RuntimeException("El nombre del articulo es requerido");
        }
        itemEntity.setItemName(getCellStringValue(nameCell).toUpperCase());
        itemEntity.setItemDescription(getCellStringValue(nameCell).toUpperCase());
        itemEntity.setBarCode(generateRandomBarCode());

        // Columna 1: Categoria (requerido)
        Cell categoryCell = row.getCell(1);
        if (categoryCell == null || categoryCell.getCellType() == CellType.BLANK) {
            throw new RuntimeException("La categoria es requerida");
        }
        // Mapear nombre de categoria a ID de catalogo (aquí podrías implementar una búsqueda)
        itemEntity.setCatalogueId(mapCategoryToId(getCellStringValue(categoryCell).toUpperCase(), catalogMap));
         // Usar nombre del artículo como descripción

        // Columna 2: Fecha de vencimiento (opcional)
        Cell expirationDateCell = row.getCell(2);
        if (expirationDateCell != null && expirationDateCell.getCellType() != CellType.BLANK) {
            itemEntity.setExpirationDate(parseDate(getCellStringValue(expirationDateCell)));
        }
        // Si no tiene fecha de vencimiento, expirationDate queda como null

        // Columna 3: Precio de Venta (requerido)
        Cell priceCell = row.getCell(3);
        if (priceCell == null || priceCell.getCellType() == CellType.BLANK) {
            throw new RuntimeException("El precio de venta es requerido");
        }
        itemEntity.setPrice(new BigDecimal(getCellNumericValue(priceCell)));

        // Columna 4: Cantidad (requerido)
        Cell quantityCell = row.getCell(4);
        if (quantityCell == null || quantityCell.getCellType() == CellType.BLANK) {
            throw new RuntimeException("La cantidad es requerida");
        }
        itemEntity.setQuantity((int) getCellNumericValue(quantityCell));

        // Valores por defecto
        itemEntity.setSoldQuantity(0);
        itemEntity.setStatus(true);
        itemEntity.setVersion(1);
        itemEntity.setRegisterUserDate("system");

        return itemEntity;
    }

    private String getCellStringValue(Cell cell) {
        switch (cell.getCellType()) {
            case STRING:
                return cell.getStringCellValue().trim();
            case NUMERIC:
                if (DateUtil.isCellDateFormatted(cell)) {
                    return DATE_FORMAT.format(cell.getDateCellValue());
                } else {
                    return String.valueOf((long) cell.getNumericCellValue());
                }
            case BOOLEAN:
                return String.valueOf(cell.getBooleanCellValue());
            case FORMULA:
                return cell.getCellFormula();
            default:
                return "";
        }
    }

    private double getCellNumericValue(Cell cell) {
        switch (cell.getCellType()) {
            case NUMERIC:
                return cell.getNumericCellValue();
            case STRING:
                try {
                    return Double.parseDouble(cell.getStringCellValue().trim());
                } catch (NumberFormatException e) {
                    throw new RuntimeException("Valor numérico inválido: " + cell.getStringCellValue());
                }
            default:
                throw new RuntimeException("Se esperaba un valor numérico");
        }
    }

    private Date parseDate(String dateString) throws ParseException {
        try {
            return DATE_FORMAT.parse(dateString);
        } catch (ParseException e) {
            throw new RuntimeException("Formato de fecha inválido. Se espera yyyy-MM-dd: " + dateString);
        }
    }

    private CellStyle createHeaderStyle(Workbook workbook) {
        CellStyle style = workbook.createCellStyle();
        style.setFillForegroundColor(IndexedColors.GREY_25_PERCENT.getIndex());
        style.setFillPattern(FillPatternType.SOLID_FOREGROUND);

        Font font = workbook.createFont();
        font.setBold(true);
        style.setFont(font);

        style.setBorderBottom(BorderStyle.THIN);
        style.setBorderTop(BorderStyle.THIN);
        style.setBorderLeft(BorderStyle.THIN);
        style.setBorderRight(BorderStyle.THIN);

        return style;
    }

    /**
     * Normaliza la ruta del archivo para manejar diferentes formatos y codificación URL
     *
     * @param filePath Ruta del archivo (puede contener backslashes o estar URL-encoded)
     * @return Ruta normalizada con formato correcto para el sistema operativo
     */
    private String normalizeFilePath(String filePath) {
        try {
            // Decodificar URL encoding si existe
            String decodedPath = URLDecoder.decode(filePath, "UTF-8");

            // Reemplazar forward slashes con backslashes para Windows
            String normalizedPath = decodedPath.replace('/', '\\');

            log.debug("Original path: {}, Decoded: {}, Normalized: {}", filePath, decodedPath, normalizedPath);

            return normalizedPath;
        } catch (UnsupportedEncodingException e) {
            log.warn("Error decoding file path, using original: {}", e.getMessage());
            // Si falla la decodificación, reemplazar caracteres manualmente
            return filePath.replace("%5C", "\\").replace("/", "\\");
        }
    }

    /**
     * Busca el ID del catálogo por nombre de categoría en la base de datos.
     * Realiza búsqueda case-insensitive y solo retorna catálogos activos.
     *
     * @param categoryName Nombre de la categoría
     * @param catalogMap
     * @return ID del catálogo correspondiente
     * @throws RuntimeException si no encuentra la categoría
     */
    private Long mapCategoryToId(String categoryName, Map<String, Long> catalogMap) {
        try {
            
            // Buscar por nombre en mayúsculas (case insensitive)
            Long catalogId = catalogMap.get(categoryName.trim().toUpperCase());
            
            if (catalogId == null) {
                throw new RuntimeException("Categoría no encontrada: '" + categoryName + "'. Debe crearla primero en el sistema.");
            }
            
            log.debug("Categoría '{}' encontrada con ID: {}", categoryName, catalogId);
            return catalogId;
            
        } catch (Exception e) {
            log.error("Error buscando categoría '{}': {}", categoryName, e.getMessage());
            throw new RuntimeException("Error al buscar categoría '" + categoryName + "': " + e.getMessage());
        }
    }

    /**
     * Genera un código de barras aleatorio de 13 dígitos como String.
     * Formato similar a códigos EAN-13.
     *
     * @return String con 13 dígitos numéricos
     */
    private String generateRandomBarCode() {
        Random random = new Random();
        StringBuilder barCode = new StringBuilder();
        
        // Generar 12 dígitos aleatorios
        for (int i = 0; i < 12; i++) {
            barCode.append(random.nextInt(10));
        }
        
        // Calcular dígito de control (simplificado)
        int sum = 0;
        for (int i = 0; i < 12; i++) {
            int digit = Character.getNumericValue(barCode.charAt(i));
            sum += (i % 2 == 0) ? digit : digit * 3;
        }
        int checkDigit = (10 - (sum % 10)) % 10;
        barCode.append(checkDigit);
        
        return barCode.toString();
    }
}
