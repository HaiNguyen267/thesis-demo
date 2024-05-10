package com.hainguyen.orderservice.config.kafka;

import com.hainguyen.orderservice.events.downstream.ShippingInitiated;
import com.hainguyen.orderservice.events.upstream.InventoryChecked;
import com.hainguyen.orderservice.events.upstream.PaymentVerified;
import com.hainguyen.orderservice.events.upstream.ShippingStatusUpdated;
import com.hainguyen.orderservice.service.NotificationService;
import com.hainguyen.orderservice.service.OrderService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import reactor.core.publisher.Flux;

import javax.management.Notification;
import java.util.function.Consumer;

@Configuration
@RequiredArgsConstructor
@Log4j2
public class ConsumerBindingConfig {

    private final OrderService orderService;
    private final NotificationService notificationService;


    @Bean
    public Consumer<Flux<InventoryChecked>> upstreamCheckInventory() {
        return flux -> flux
                .doOnNext(response -> log.info("Received inventory check response: {}", response))
                .flatMap(response -> {
                    if (Boolean.TRUE.equals(response.getInStock())) {
                        // if the product is in stock, update the order status and start the payment verification process
                        return orderService
                                .processInventoryCheckResult(response)
                                .doOnNext(order -> log.info("Order: {}", order))
                                .flatMap(order -> orderService.processPaymentVerification(order.getId()));
                    } else {
                        // if the product is out of stock, update the order status and return the order
                        return orderService
                                .processInventoryCheckResult(response)
                                .flatMap(notificationService::sendNotificationOrderOnHold);
                    }
                })
                .subscribe();


    }

    @Bean
    public Consumer<Flux<PaymentVerified>> upstreamPaymentVerification() {
        return flux -> flux
                .doOnNext(response -> log.info("Received payment verification response: {}", response))
                .flatMap(response -> {
                    if (response.getVerified()) {
                        // if the payment is successful, update the order status and start the shipping process
                        return orderService
                                .processPaymentVerificationResult(response)
                                .flatMap(order -> orderService.processShipping(order.getId()))
                                .flatMap(order -> orderService.updateInventory(order));
                    } else {
                        // if the payment is failed, update the order status and return the order
                        return orderService
                                .processPaymentVerificationResult(response)
                                .flatMap(notificationService::sendNotificationOrderOnHold);
                    }
                })
                .subscribe();
    }

    @Bean
    public Consumer<Flux<ShippingStatusUpdated>> upstreamShipping() {
        return flux -> flux
                .doOnNext(response -> log.info("Received shipping response: {}", response))
                .flatMap(orderService::updateOrderShippingStatus)
                .flatMap(notificationService::sendNotificationOrderShippingStatus)
                .subscribe();
    }

}
