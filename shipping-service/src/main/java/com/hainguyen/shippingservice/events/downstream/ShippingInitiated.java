package com.hainguyen.shippingservice.events.downstream;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ShippingInitiated {
    private Integer orderId;
    private String address;
}
