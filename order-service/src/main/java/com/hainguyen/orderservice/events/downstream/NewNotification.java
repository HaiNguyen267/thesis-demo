package com.hainguyen.orderservice.events.downstream;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class NewNotification {
    private Integer orderId;
    private Integer userId;
    private String status;
    private String timestamp;
}
