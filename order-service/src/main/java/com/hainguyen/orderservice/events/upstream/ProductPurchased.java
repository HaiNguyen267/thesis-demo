package com.hainguyen.orderservice.events.upstream;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ProductPurchased {
    private Integer orderId;
    private Integer productId;
    private Integer quantity;
}
