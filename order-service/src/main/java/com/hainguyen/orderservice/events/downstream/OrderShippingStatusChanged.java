package com.hainguyen.orderservice.events.downstream;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

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
