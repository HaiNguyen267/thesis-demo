package com.hainguyen.notificationservice.events.downstream;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserContactRequest {
    private Integer orderId;
    private Integer userId;
}
