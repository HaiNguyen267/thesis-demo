package com.hainguyen.notificationservice.events.upstream;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class NewNotificationEvent {
    private Integer orderId;
    private Integer userId;
    private String status;
    private String timestamp;
}
