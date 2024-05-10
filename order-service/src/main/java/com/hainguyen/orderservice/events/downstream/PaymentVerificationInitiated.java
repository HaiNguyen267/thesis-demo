package com.hainguyen.orderservice.events.downstream;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PaymentVerificationInitiated {
    private Integer orderId;
    private Double amount;
    private String cardNumber;
}
