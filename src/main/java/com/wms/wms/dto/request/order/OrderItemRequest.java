package com.wms.wms.dto.request.order;

import jakarta.validation.constraints.Digits;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
public class OrderItemRequest {
    private Long id;

    private Long productId;

    @Digits(integer = 10, fraction = 6, message = "OrderItemRequest quantity must be decimal")
    private BigDecimal quantity;
}
