package com.inventory.store.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.Date;

/**
 * VO que representa un historial de venta.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class HistoricalSaleVO {

    private String uuid;
    private Long itemCode;
    private String itemName;
    private Integer previousQuantity;
    private Integer newQuantity;
    private Integer soldQuantity;
    private String registeredBy;
    private Date registrationDate;
}
