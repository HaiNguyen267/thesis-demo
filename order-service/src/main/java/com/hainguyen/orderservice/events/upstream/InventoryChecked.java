package com.hainguyen.orderservice.events.upstream;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class InventoryChecked {
    private Integer orderId;
    private Integer productId;
    private Boolean inStock;
    private String message;
    private Double totalPrice;
}
