package com.inventory.store.core.service;

import com.inventory.store.client.entity.ItemEntity;
import com.inventory.store.client.repository.IHistoricalSaleRepository;
import com.inventory.store.client.repository.IItemRepository;
import com.inventory.store.client.services.IHistoricalSaleService;
import com.inventory.store.core.component.UserContextService;
import com.inventory.store.request.VentaItemVO;
import com.inventory.store.request.VentaMultipleVO;
import com.inventory.store.vo.HistoricalSaleVO;
import com.inventory.store.vo.HistoricalSalesResponseVO;
import com.inventory.store.vo.MovementVO;
import com.inventory.store.vo.ProductoVO;
import com.inventory.store.vo.StockRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@Slf4j
@Service
public class ISaleService implements com.inventory.store.client.services.ISaleService {

    @Autowired
    @Lazy
    private final IItemRepository itemRepository;

    @Autowired
    @Lazy
    private final IHistoricalSaleRepository historicalSaleRepository;

    @Autowired
    @Lazy
    private final IHistoricalSaleService historialVentaService;

    @Autowired
    @Lazy
    private final UserContextService userContextService;

    public ISaleService(IItemRepository itemRepository, IHistoricalSaleRepository historicalSaleRepository, IHistoricalSaleService historialVentaService, UserContextService userContextService) {
        this.itemRepository = itemRepository;
        this.historicalSaleRepository = historicalSaleRepository;
        this.historialVentaService = historialVentaService;
        this.userContextService = userContextService;
    }

    @Override
    @Transactional
    public void registrarVentaMultiple(VentaMultipleVO ventaRequest) {
        log.info("Procesando venta múltiple con {} productos", ventaRequest.getItems().size());

        List<VentaItemVO> items = ventaRequest.getItems();

        // Procesar cada item de la venta
        for (VentaItemVO item : items) {
            log.info("Procesando item: código={}, cantidad={}, precio={}",
                    item.getItemCode(), item.getCantidad(), item.getPrecioUnitario());

            // Buscar el producto en la base de datos
            ItemEntity itemEntity = itemRepository.findById(item.getItemCode());

            if (itemEntity == null) {
                throw new RuntimeException("Producto no encontrado con código: " + item.getItemCode());
            }

            // Verificar stock disponible
            if (itemEntity.getQuantity() < item.getCantidad()) {
                throw new RuntimeException("Stock insuficiente para el producto " + itemEntity.getItemName() +
                        ". Disponible: " + itemEntity.getQuantity() + ", Solicitado: " + item.getCantidad());
            }

            // Guardar cantidad anterior para el historial
            int previousQuantity = itemEntity.getQuantity();

            // Actualizar stock y cantidad vendida
            int newQuantity = itemEntity.getQuantity() - item.getCantidad();
            int cantidadVendidaActual = itemEntity.getSoldQuantity() != null ? itemEntity.getSoldQuantity() : 0;
            int nuevaCantidadVendida = cantidadVendidaActual + item.getCantidad();

            itemEntity.setQuantity(newQuantity);
            itemEntity.setSoldQuantity(nuevaCantidadVendida);

            // Actualizar la entidad existente con datos de auditoría (fecha actualización, etc.)
            itemRepository.update(itemEntity);

            log.info("Producto actualizado: {} - Nuevo stock: {}, Total vendidos: {}",
                    itemEntity.getItemName(), newQuantity, nuevaCantidadVendida);

            // Guardar historial de la venta
            historialVentaService.saveHistoricalSale(
                    itemEntity.getItemCode(),
                    itemEntity.getItemName(),
                    previousQuantity,
                    newQuantity,
                    item.getCantidad(),
                    userContextService.getCurrentUser() // Obtener el usuario actual del contexto de seguridad
            );

            //log.info("Producto actualizado: {} - Nuevo stock: {}, Total vendidos: {}",
            //        itemEntity.getItemName(), newQuantity, nuevaCantidadVendida);
        }

        log.info("Venta múltiple procesada exitosamente. Total productos: {}", items.size());
    }

    @Override
    public HistoricalSalesResponseVO getHistoricalSalesItem(Long itemCode) {
        log.info("Obteniendo histórico de ventas para el producto: {}", itemCode);

        // Buscar el producto en la base de datos
        ItemEntity producto = itemRepository.findById(itemCode);
        
        if (producto == null) {
            throw new RuntimeException("Producto no encontrado con código: " + itemCode);
        }

        log.info("Producto encontrado: {}", producto.getItemName());

        // Crear el VO del producto

        ProductoVO productoVO = ProductoVO.builder()
                .itemCode(producto.getItemCode())
                .name(producto.getItemName())
                .description(producto.getItemDescription())
                .barCode(producto.getBarCode())
                .build();

        // Consultar el histórico real desde INV_SALE_HISTORY
        List<MovementVO> movimientos = new ArrayList<>();
        
        try {
            // Obtener todas las ventas históricas para este itemCode usando el método del repositorio
            List<HistoricalSaleVO> ventasHistoricas = historicalSaleRepository.getHistoricalItems(itemCode);
            
            log.info("Se encontraron {} ventas históricas para el producto {}", ventasHistoricas.size(), itemCode);
            
            // Convertir cada venta histórica a MovimientoVO
            for (HistoricalSaleVO venta : ventasHistoricas) {
                MovementVO movimiento = MovementVO.builder()
                        .id(venta.getUuid() != null ? venta.getUuid() : UUID.randomUUID().toString())
                        .type("sale")
                        .quantity(venta.getSoldQuantity())
                        .user(venta.getRegisteredBy())
                        .date(venta.getRegistrationDate() != null ?
                                venta.getRegistrationDate().toString() : new Date().toString())
                        .description("Venta de " + venta.getSoldQuantity() + " unidades")
                        .reference("Venta-" + (venta.getUuid() != null ? venta.getUuid() : UUID.randomUUID().toString().substring(0, 8)))
                        .previousQuantity(venta.getPreviousQuantity())
                        .newQuantity(venta.getNewQuantity())
                        .build();
                
                movimientos.add(movimiento);
            }
            
        } catch (Exception e) {
            log.error("Error al consultar ventas históricas para el producto {}: {}", itemCode, e.getMessage(), e);
            // Si hay error con la tabla, generar datos simulados como fallback
            if (producto.getSoldQuantity() != null && producto.getSoldQuantity() > 0) {
                MovementVO movementSale = MovementVO.builder()
                        .id(UUID.randomUUID().toString())
                        .type("sale")
                        .quantity(producto.getSoldQuantity())
                        .user("System")
                        .date(LocalDateTime.now().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME))
                        .description("Sale of " + producto.getSoldQuantity() + " units (simulated)")
                        .reference("SALE-" + UUID.randomUUID().toString().substring(0, 8))
                        .build();
                
                movimientos.add(movementSale);
            }
        }

        // Construir el response completo
        HistoricalSalesResponseVO response = HistoricalSalesResponseVO.builder()
                .item(productoVO)
                .movements(movimientos)
                .totalMovements(movimientos.size())
                .build();

        log.info("Histórico construido para el producto {} - Total movimientos: {}", 
                itemCode, response.getTotalMovements());

        return response;
    }

    @Override
    @Transactional
    public void addStockItem(StockRequest stockRequest) {
        log.info("Agregando stock al producto: {} - Cantidad: {}",
                stockRequest.getItemCode(), stockRequest.getQuantity());

        // Buscar el producto en la base de datos
        ItemEntity itemEntity = itemRepository.findById(stockRequest.getItemCode());

        if (itemEntity == null) {
            throw new RuntimeException("Producto no encontrado con código: " + stockRequest.getItemCode());
        }

        // Guardar cantidad anterior para el historial
        int previousQuantity = itemEntity.getQuantity() != null ? itemEntity.getQuantity() : 0;

        // Actualizar stock
        int newQuantity = previousQuantity + stockRequest.getQuantity();
        itemEntity.setQuantity(newQuantity);

        // Actualizar la entidad existente
        itemRepository.update(itemEntity);

        log.info("Stock actualizado: {} - Cantidad anterior: {}, Nueva cantidad: {}", 
                itemEntity.getItemName(), previousQuantity, newQuantity);

        // Guardar historial del agregado de stock
        historialVentaService.saveHistoricalSale(
                itemEntity.getItemCode(),
                itemEntity.getItemName(),
                previousQuantity,
                newQuantity,
                stockRequest.getQuantity(),
                userContextService.getCurrentUser()
        );

        log.info("Stock agregado exitosamente. Producto: {}, Cantidad agregada: {}", 
                itemEntity.getItemName(), stockRequest.getQuantity());
    }
}
