package com.hainguyen.orderservice.dto.request;

import lombok.Data;

@Data
public class OrderUpdateRequest {
    private Integer orderId;
    private String cardNumber;
    private String shippingAddress;
}
