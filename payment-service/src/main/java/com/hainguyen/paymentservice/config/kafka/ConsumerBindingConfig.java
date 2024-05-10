package com.hainguyen.paymentservice.config.kafka;

import com.hainguyen.paymentservice.events.downstream.PaymentVerificationInitiated;
import com.hainguyen.paymentservice.events.downstream.ProductReturned;
import com.hainguyen.paymentservice.events.upstream.PaymentVerified;
import com.hainguyen.paymentservice.service.PaymentService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import reactor.core.publisher.Flux;

import java.util.function.Consumer;
import java.util.function.Function;

@Configuration
@RequiredArgsConstructor
@Log4j2
public class ConsumerBindingConfig {

    private final PaymentService paymentService;


    @Bean
    public Function<Flux<PaymentVerificationInitiated>, Flux<PaymentVerified>> upstreamVerifyPayment() {
        return flux -> flux
                .doOnNext(payment -> log.info("Received payment to verify payment: {}", payment))
                .flatMap(paymentService::verifyPayment)
                .doOnNext(payment -> log.info("Payment verified: {}", payment))
                ;
    }


    @Bean
    public Consumer<Flux<ProductReturned>> downstreamProductReturned() {
        return flux -> flux
                .doOnNext(productReturned -> log.info("Received product returned: {}", productReturned))
                .flatMap(paymentService::processProductReturned)
                .subscribe();
    }



}
