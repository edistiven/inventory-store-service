package com.inventory.store.vo;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CatalogVO implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long id;

    @NotBlank(message = "The catalog name is required")
    @Size(max = 100, message = "The name cannot exceed 100 characters")
    private String name;

    @Size(max = 500, message = "The description cannot exceed 500 characters")
    private String description;

    @NotNull(message = "The active status is required")
    private Boolean status;

    private Date registerDate;

    private String registerUserDate;

    private Date modificationDate;

    private String modificationUserDate;
}
