package com.inventory.store.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

/**
 * VO para las estadísticas del inventario
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EstadisticasInventarioVO {
    
    private Long totalItems;
    
    private Integer stockTotal;
    
    private BigDecimal valorTotal;
    
    private Integer stockSaludable;
    
    private Integer stockBajo;
    
    private Double promedioStockPorProducto;
    
    private BigDecimal valorPromedioPorProducto;
}
