package com.inventory.store.vo;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.Date;

@Data
@NoArgsConstructor
public class StockRequest {
    
    @NotNull(message = "El código del producto es requerido")
    @Min(value = 1, message = "El código del producto debe ser mayor a 0")
    private Integer itemCode;
    
    @NotNull(message = "La cantidad es requerida")
    @Min(value = 1, message = "La cantidad debe ser mayor a 0")
    private Integer quantity;
    
    @NotNull(message = "La fecha de registro es requerida")
    private Date registerDate;
    
    public StockRequest(Integer itemCode, Integer quantity, String user, Date registerDate) {
        this.itemCode = itemCode;
        this.quantity = quantity;
        this.registerDate = registerDate;
    }
}
