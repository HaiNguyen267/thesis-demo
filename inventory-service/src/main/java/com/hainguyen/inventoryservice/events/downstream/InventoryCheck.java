package com.hainguyen.inventoryservice.events.downstream;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class InventoryCheck {
    private Integer orderId;
    private Integer productId;
    private Double quantity;
}
