package com.inventory.store.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MovementVO {
    private String id;
    private String type; // 'sale', 'entry', 'exit', 'adjustment'
    private Integer quantity;
    private String user;
    private String date;
    private String description;
    private String reference;
    private Integer previousQuantity;
    private Integer newQuantity;
}
