package com.inventory.store.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProductoVO {
    private Long itemCode;
    private String name;
    private String description;
    private String barCode;
}
