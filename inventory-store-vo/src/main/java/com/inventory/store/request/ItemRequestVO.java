package com.inventory.store.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Max;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
public class ItemRequestVO {

    @Builder.Default
    @Min(value = 0, message = "La página no puede ser negativa")
    private int page = 0;

    @Builder.Default
    @Min(value = 1, message = "El tamaño mínimo es 1")
    @Max(value = 100, message = "El tamaño máximo es 100")
    private int size = 10;

    private String barCode;
    private String name;
    private String description;
    private Boolean status;
}
