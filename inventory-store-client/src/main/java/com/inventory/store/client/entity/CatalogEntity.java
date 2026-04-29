package com.inventory.store.client.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import com.inventory.store.client.converter.BooleanToCharConverter;
import jakarta.persistence.Convert;

import java.util.Date;

/**
 * Entity class for Catalog.
 *
 * @author eucsina on 27/04/2026
 */
@Entity
@Table(name = "INV_CATALOG")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CatalogEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID")
    private Long id;

    @Column(name = "NAME", nullable = false, length = 100)
    private String name;

    @Column(name = "DESCRIPTION", length = 500)
    private String description;

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
        if (status == null) {
            status = true;
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
}
