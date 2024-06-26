package com.wms.wms.dto.request;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;

@Getter
public class ProductRequest {
    @Setter
    private int id;

    @NotBlank(message = "Product name cannot be blank")
    @Size(min = 1, max = 255, message = "Product name must be between 1 and 255 characters")
    private String name;

    @Size(max = 255, message = "Product description must be under 256 characters")
    private  String description;

    @Size(max = 63, message = "Product code must be under 63 characters")
    private String code;

    @Size(max = 63, message = "Product uom must be under 63 characters")
    private String uom;

    private String customFields;

    @Lob
    private byte[] images;

    private int categoryId;
}
