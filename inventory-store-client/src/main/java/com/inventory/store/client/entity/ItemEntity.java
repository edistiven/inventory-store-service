package com.inventory.store.client.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.Date;

import com.inventory.store.client.converter.BooleanToCharConverter;
import jakarta.persistence.Convert;

/**
 * Entity class for Item.
 *
 * @author eucsina on 24/04/2026
 */
@Entity
@Table(name = "INV_ITEM")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ItemEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ITEM_CODE")
    private Long itemCode;

    @Column(name = "NAME", nullable = false, length = 200)
    private String itemName;

    @Column(name = "DESCRIPTION", length = 500)
    private String itemDescription;

    @Column(name = "PRICE", nullable = false, precision = 12, scale = 2)
    private BigDecimal price;

    @Column(name = "QUANTITY", nullable = false)
    private Integer quantity;

    @Column(name = "EXPIRATION_DATE", nullable = false)
    private Date expirationDate;

    @Column(name = "SOLD_QUANTITY", nullable = false)
    private Integer soldQuantity;

    @Column(name = "BAR_CODE", length = 15)
    private String barCode;

    @Column(name = "CATALOG_ID")
    private Long catalogueId;

    @Column(name = "STATUS", nullable = false)
    @Convert(converter = BooleanToCharConverter.class)
    @NotNull
    private Boolean status;

    @Column(name = "VERSION", nullable = false)
    private Integer version;

    @Column(name = "REGISTER_DATE", nullable = false, updatable = false)
    private Date registerDate;

    @Column(name = "REGISTER_USER_DATE", nullable = false, length = 50)
    private String registerUserDate;

    @Column(name = "MODIFICATION_DATE")
    private Date modificationDate;

    @Column(name = "MODIFICATION_USER_DATE", length = 50)
    private String modificationUserDate;

    @PrePersist
    protected void onCreate() {
        registerDate = new Date();
        if (soldQuantity == null) {
            soldQuantity = 0;
        }
        if (status == null) {
            status = true; // Valor por defecto '1'
        }
        if (version == null) {
            version = 1;
        }
    }

    @PreUpdate
    protected void onUpdate() {
        modificationDate = new Date();
        if (version != null) {
            version++;
        }
    }

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "CATALOG_ID", insertable = false, updatable = false)
    private CatalogEntity catalogEntity;
}
