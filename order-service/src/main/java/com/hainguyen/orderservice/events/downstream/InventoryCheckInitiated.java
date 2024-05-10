package com.hainguyen.orderservice.events.downstream;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class InventoryCheckInitiated {
    private Integer orderId;
    private Integer productId;
    private Integer quantity;
}
