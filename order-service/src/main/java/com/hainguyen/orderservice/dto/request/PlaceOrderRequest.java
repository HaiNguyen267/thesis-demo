package com.hainguyen.orderservice.dto.request;

import lombok.Data;

@Data
public class PlaceOrderRequest {
    private int productId;
    private int userId;
    private int quantity;
    private String cardNumber;
    private String shippingAddress;
}
