package com.hainguyen.shippingservice.dto.request;

import lombok.Data;

@Data
public class OrderReturnedRequest {
    private int orderId;
    private String reason;
}
