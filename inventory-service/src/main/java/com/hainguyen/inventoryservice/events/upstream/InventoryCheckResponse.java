package com.hainguyen.inventoryservice.events.upstream;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class InventoryCheckResponse {
    private Integer orderId;
    private Integer productId;
    private boolean inStock;
    private String message;
    private Double totalPrice;
}
