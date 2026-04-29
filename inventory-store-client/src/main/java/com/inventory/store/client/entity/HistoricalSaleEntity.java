package com.inventory.store.client.entity;

import com.inventory.store.client.converter.BooleanToCharConverter;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * Entity class for historical sale.
 *
 * @author eucsina on 24/04/2026
 */
@Entity
@Table(name = "INV_SALE_HISTORY")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class HistoricalSaleEntity {

    @Id
    @Column(name = "UUID")
    private String uuid;

    @Column(name = "ITEM_CODE", nullable = false)
    private Long itemCode;

    @Column(name = "PREVIOUS_QUANTITY", nullable = false)
    private Integer previousQuantity;

    @Column(name = "NEW_QUANTITY", nullable = false)
    private Integer newQuantity;

    @Column(name = "SOLD_QUANTITY", nullable = false)
    private Integer soldQuantity;

    @Column(name = "REGISTERED_BY", nullable = false)
    private String registeredBy;

    @Column(name = "REGISTRATION_DATE", nullable = false)
    private Date registrationDate;

    @Column(name = "STATUS", nullable = false)
    @Convert(converter = BooleanToCharConverter.class)
    private Boolean status;

    @Column(name = "VERSION", nullable = false)
    private Integer version;

    @Column(name = "MODIFICATION_DATE")
    private Date modificationDate;

    @Column(name = "MODIFICATION_USER_DATE")
    private String modificationUserDate;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ITEM_CODE", referencedColumnName = "ITEM_CODE", insertable = false, updatable = false)
    private ItemEntity item;
}
