package com.inventory.store.client.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * Entity class for User.
 *
 * @author eucsina on 24/04/2026
 */
@Entity
@Table(name = "INV_USER")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserEntity {

    @Id
    @Column(name = "UUID")
    private String uuid;

    @NotBlank(message = "El nombre de usuario es obligatorio")
    @Size(min = 3, max = 50, message = "El nombre de usuario debe tener entre 3 y 50 caracteres")
    @Column(name = "USERNAME", nullable = false, unique = true, length = 50)
    private String username;

    @NotBlank(message = "La contraseña es obligatoria")
    @Size(min = 6, message = "La contraseña debe tener al menos 6 caracteres")
    @Column(name = "PASSWORD", nullable = false)
    private String password;

    @NotBlank(message = "El rol es obligatorio")
    @Column(name = "ROL", nullable = false, length = 20)
    private String rol;

    @Column(name = "USER_COMPLETE_NAME", length = 255)
    private String userCompleteName;

    @Column(name = "STATUS", nullable = false)
    private Character status;

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
        if (uuid == null) {
            uuid = java.util.UUID.randomUUID().toString();
        }
        registerDate = new Date();
        if (status == null) {
            status = '1';
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
