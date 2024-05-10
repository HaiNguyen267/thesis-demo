package com.hainguyen.orderservice.entity;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name = "orders")
public class Order {
    @Id
    private Integer id;
    @Column("user_id")
    private Integer userId;
    @Column("product_id")
    private Integer productId;
    private Integer quantity;
    @Column("total_price")
    private Double totalPrice;
    private OrderStatus status;
    @Column("card_number")
    private String cardNumber;

    @Column("payment_verified")
    private boolean paymentVerified;
    @Column("shipping_address")
    private String shippingAddress;

    @Builder.Default
    private LocalDateTime createdAt = LocalDateTime.now();
    @Builder.Default
    private LocalDateTime updatedAt = LocalDateTime.now();

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
}
