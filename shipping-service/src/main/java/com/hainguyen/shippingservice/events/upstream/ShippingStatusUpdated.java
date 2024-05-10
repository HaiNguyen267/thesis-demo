package com.hainguyen.shippingservice.events.upstream;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ShippingStatusUpdated {
    private Integer orderId;
    private String status;
    private String message;
}
