package com.inventory.store.request;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * Value Object para representar una venta múltiple con varios productos
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class VentaMultipleVO {
    
    @NotEmpty(message = "Los items de la venta son requeridos")
    private List<VentaItemVO> items;
    
    @NotNull(message = "El total de la venta es requerido")
    private Double totalVenta;
    
    @NotNull(message = "La fecha de venta es requerida")
    private String fechaVenta;
    
    private String cliente; // Opcional para futuras funcionalidades
}
