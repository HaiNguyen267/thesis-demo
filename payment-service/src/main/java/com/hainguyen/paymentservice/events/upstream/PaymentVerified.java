package com.hainguyen.paymentservice.events.upstream;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PaymentVerified {
    private Integer orderId;
    private Boolean verified;
    private String message;
}
