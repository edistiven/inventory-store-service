package com.inventory.store.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class HistoricalSalesResponseVO {

    private ProductoVO item;
    private List<MovementVO> movements;
    private Integer totalMovements;
}
