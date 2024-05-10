package com.hainguyen.notificationservice.model;

public enum OrderStatus {
    PENDING,
    PROCESSING_CHECK_INVENTORY,
    PROCESSING_VERIFY_PAYMENT,
    PROCESSING_SHIPPING,

    ON_HOLD_OUT_OF_STOCK,
    ON_HOLD_PAYMENT_FAILED,
    ON_HOLD_SHIPPING,

    SHIPPED,
    CANCELLED,
    DELIVERED,
    RETURNED;


    public static OrderStatus fromString(String status) {
        for (OrderStatus orderStatus : OrderStatus.values()) {
            if (orderStatus.name().equalsIgnoreCase(status)) {
                return orderStatus;
            }
        }
        return null;
    }
}