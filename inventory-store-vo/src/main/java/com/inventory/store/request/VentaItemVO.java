package com.inventory.store.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Value Object para representar un item individual en una venta
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class VentaItemVO {
    
    @NotNull(message = "El código del producto es requerido")
    private Long itemCode;
    
    @NotNull(message = "La cantidad es requerida")
    @Min(value = 1, message = "La cantidad debe ser mayor a 0")
    private Integer cantidad;
    
    @NotNull(message = "El precio unitario es requerido")
    private Double precioUnitario;
    
    @NotNull(message = "El subtotal es requerido")
    private Double subtotal;
}
