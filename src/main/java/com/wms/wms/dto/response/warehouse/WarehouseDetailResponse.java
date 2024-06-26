package com.wms.wms.dto.response.warehouse;


import lombok.Builder;
import lombok.Getter;

import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Timestamp;

@Getter
@Builder
public class WarehouseDetailResponse  implements Serializable {
    private int id;
    private String name;
    private  String description;
    private String address;
    private BigDecimal longitude;
    private BigDecimal latitude;
    private  String supervisor;
    private  String status;
    private Timestamp createdAt;
    private  Timestamp modifiedAt;
}
