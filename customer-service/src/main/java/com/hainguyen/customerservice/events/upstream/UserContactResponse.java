package com.hainguyen.customerservice.events.upstream;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserContactResponse {
    private Integer orderId;
    private Integer userId;
    private String email;
}
