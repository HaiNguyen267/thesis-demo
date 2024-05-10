package com.hainguyen.paymentservice.service;

import com.hainguyen.paymentservice.events.downstream.PaymentVerificationInitiated;
import com.hainguyen.paymentservice.events.downstream.ProductReturned;
import com.hainguyen.paymentservice.events.upstream.PaymentVerified;
import lombok.extern.slf4j.Slf4j;
import org.reactivestreams.Publisher;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
@Slf4j
public class PaymentService {
    public Mono<PaymentVerified> verifyPayment(PaymentVerificationInitiated paymentVerificationInitiated) {
        return Mono.just(paymentVerificationInitiated)
                .map(payment -> {
                    int amount = paymentVerificationInitiated.getAmount().intValue();
                    String cardNumber = paymentVerificationInitiated.getCardNumber();

                    boolean verifiedCardNumber = verifyCardNumber(cardNumber);
                    log.info("Payment verification initiated for order: {} with amount: {} and card number: {} success: {}",
                            paymentVerificationInitiated.getOrderId(),
                            amount,
                            cardNumber,
                            verifiedCardNumber);

                    if (amount > 0 && verifiedCardNumber) {
                        return PaymentVerified.builder()
                                .orderId(paymentVerificationInitiated.getOrderId())
                                .verified(true)
                                .message("Payment verified successfully")
                                .build();
                    } else {
                        return PaymentVerified.builder()
                                .orderId(paymentVerificationInitiated.getOrderId())
                                .verified(false)
                                .message("Payment verification failed")
                                .build();
                    }
                });
    }


    private boolean verifyCardNumber(String cardNumber) {
        if (cardNumber == null || cardNumber.length() != 16) {
            return false;
        }
        int nDigits = cardNumber.length();
        int sum = 0;
        boolean alternate = false;
        for (int i = nDigits - 1; i >= 0; i--) {
            int n = Integer.parseInt(cardNumber.substring(i, i + 1));
            if (alternate) {
                n *= 2;
                if (n > 9) {
                    n = (n % 10) + 1;
                }
            }
            sum += n;
            alternate = !alternate;
        }
        return (sum % 10 == 0);
    }

    public Mono<Void> processProductReturned(ProductReturned productReturned) {
        return Mono.just(productReturned)
                .doOnNext(returned -> log.info("Order id {} is returned processing to refund customer", productReturned.getOrderId()))
                .then();
    }
}
