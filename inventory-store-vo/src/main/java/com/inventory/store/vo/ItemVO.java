package com.inventory.store.vo;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Date;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ItemVO implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long itemCode;

    @NotBlank(message = "El código de barras es obligatorio")
    @Size(max = 15, message = "El código de barras no puede exceder 15 caracteres")
    private String barCode;

    @NotBlank(message = "El nombre del producto es obligatorio")
    @Size(max = 200, message = "El nombre no puede exceder 200 caracteres")
    private String name;

    @Size(max = 500, message = "La descripción no puede exceder 500 caracteres")
    private String description;

    @NotNull(message = "El precio es obligatorio")
    @DecimalMin(value = "0.01", message = "El precio debe ser mayor a 0")
    @Digits(integer = 10, fraction = 2, message = "El precio puede tener máximo 2 decimales")
    private BigDecimal price;

    @NotNull(message = "La cantidad es obligatoria")
    @Min(value = 0, message = "La cantidad no puede ser negativa")
    private Integer quantity;

    @NotNull(message = "El ID del catálogo es obligatorio")
    private Long catalogueId;
    
    private String catalogueName;

    private Date expirationDate;

    private Integer quantitySold;
    
    private Boolean status;

}
