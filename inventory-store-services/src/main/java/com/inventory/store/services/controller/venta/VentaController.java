package com.inventory.store.services.controller.venta;

import com.inventory.store.request.VentaMultipleVO;
import com.inventory.store.services.controller.base.ApiResponse;
import com.inventory.store.client.services.ISaleService;
import com.inventory.store.vo.HistoricalSalesResponseVO;
import com.inventory.store.vo.StockRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * Controlador para ventas.
 */
@RestController
@RequestMapping("/api/ventas")
@RequiredArgsConstructor
@Slf4j
public class VentaController {

    @Autowired
    @Lazy
    private final ISaleService ventaService;

    /**
     * Registar venta.
     *
     * @param ventaRequest
     * @return
     */
    @PostMapping("/registerSale")
    public ResponseEntity<ApiResponse<String>> registrarVentaMultiple(@Valid @RequestBody VentaMultipleVO ventaRequest) {
        try {
            // Llamar al servicio para procesar la venta
            ventaService.registrarVentaMultiple(ventaRequest);
            return ResponseEntity.ok(ApiResponse.success("Venta registrada exitosamente", "Venta múltiple procesada correctamente"));

        } catch (Exception e) {
            log.error("Error al registrar venta múltiple: {}", ventaRequest, e);
            return ResponseEntity.internalServerError()
                    .body(ApiResponse.error("Error al registrar la venta: " + e.getMessage()));
        }
    }

    /**
     * Agregar stock a un producto.
     *
     * @param stockRequest
     * @return
     */
    @PostMapping("/addStockItem")
    public ResponseEntity<ApiResponse<String>> agregarStock(@Valid @RequestBody StockRequest stockRequest) {
        try {
            log.info("Agregando stock al producto: {} - Cantidad: {}", stockRequest.getItemCode(), stockRequest.getQuantity());
            
            // Llamar al servicio para agregar stock
            ventaService.addStockItem(stockRequest);
            
            return ResponseEntity.ok(ApiResponse.success("Stock agregado exitosamente", "Stock actualizado correctamente"));

        } catch (Exception e) {
            log.error("Error al agregar stock: {}", stockRequest, e);
            return ResponseEntity.internalServerError()
                    .body(ApiResponse.error("Error al agregar stock: " + e.getMessage()));
        }
    }

    /**
     * Obtiene el histórico de ventas para un producto específico.
     *
     * @param itemCode Código del producto
     * @return VO con el histórico completo del producto
     */
    @GetMapping("/getHistoricalItems/{itemCode}")
    public ResponseEntity<ApiResponse<HistoricalSalesResponseVO>> getHistoricalItems(@PathVariable Long itemCode) {
        try {
            log.info("Obteniendo histórico de ventas para el producto: {}", itemCode);

            // Llamar al servicio para obtener el histórico
            HistoricalSalesResponseVO historico = ventaService.getHistoricalSalesItem(itemCode);

            log.info("Histórico obtenido exitosamente para el producto: {} - Total movimientos: {}",
                    itemCode, historico.getTotalMovements());

            return ResponseEntity.ok(ApiResponse.success(historico, "Histórico obtenido exitosamente"));

        } catch (Exception e) {
            log.error("Error al obtener histórico de ventas para el producto {}: {}", itemCode, e.getMessage(), e);
            return ResponseEntity.internalServerError()
                    .body(ApiResponse.error("Error al obtener el histórico de ventas: " + e.getMessage()));
        }
    }
}
