package com.wms.wms.dto.response.product;

import com.wms.wms.dto.response.BaseEntityResponse;
import com.wms.wms.entity.Product;
import com.wms.wms.entity.Warehouse;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
public class ProductWarehouseResponse extends BaseEntityResponse {
    private Product product;
    private Warehouse warehouse;
    private BigDecimal quantityOnHand = BigDecimal.ZERO;
}
