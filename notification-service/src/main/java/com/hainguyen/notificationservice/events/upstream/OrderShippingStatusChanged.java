package com.hainguyen.notificationservice.events.upstream;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class OrderShippingStatusChanged {
    private Integer orderId;
    private Integer userId;
    private String status;
    private String timestamp;
}
