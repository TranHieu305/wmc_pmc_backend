package com.wms.wms.dto.request;

import jakarta.persistence.Column;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;

import java.io.Serializable;
import java.math.BigDecimal;

@Getter
public class WarehouseRequest implements Serializable {
    private int id;

    @Column(name = "name")
    @NotBlank(message = "Warehouse name cannot be blank")
    @Size(min = 1, max = 255, message = "Warehouse name must be between 1 and 255 characters")
    private String name;

    @Column(name = "description")
    @NotBlank(message = "Warehouse description cannot be blank")
    @Size(min = 1, max = 255, message = "Warehouse description must be between 1 and 255 characters")
    private  String description;

    @Column(name = "address")
    @NotBlank(message = "Warehouse address cannot be blank")
    @Size(min = 1, max = 255, message = "Warehouse address must be between 1 and 255 characters")
    private String address;

    @Column(name = "longitude")
    @Digits(integer = 10, fraction = 6, message = "Warehouse longitude must be decimal")
    private BigDecimal longitude;

    @Column(name = "latitude")
    @Digits(integer = 10, fraction = 6, message = "Warehouse latitude must be decimal")
    private BigDecimal latitude;

    @Column(name = "supervisor")
    private  String supervisor;

    @Column(name = "status")
    private  String status;
}
