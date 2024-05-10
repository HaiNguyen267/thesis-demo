package com.hainguyen.orderservice.config.kafka;

import com.hainguyen.orderservice.events.downstream.*;
import com.hainguyen.orderservice.events.upstream.ProductPurchased;
import com.hainguyen.orderservice.events.upstream.ProductReturned;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.Message;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Sinks;

import java.util.function.Supplier;

@Configuration
public class ProducerBindingConfig {
    @Bean
    public Supplier<Flux<Message<InventoryCheckInitiated>>> downstreamCheckInventoryOutput() {
        return () -> SinkConfig.FLUX_CHECK_INVENTORY;
    }

    @Bean
    public Supplier<Flux<Message<PaymentVerificationInitiated>>> downstreamVerifyPaymentOutput() {
        return () -> SinkConfig.FLUX_VERIFY_PAYMENT;
    }

    @Bean
    public Supplier<Flux<Message<ShippingInitiated>>> downstreamShippingOutput() {
        return () -> SinkConfig.FLUX_SHIPPING;
    }

    @Bean
    public Supplier<Flux<Message<NewNotification>>> downstreamNewNotificationOutput() {
        return () -> SinkConfig.FLUX_NOTIFY_NEW_NOTIFICATION;
    }

    @Bean
    public Supplier<Flux<Message<ProductPurchased>>> downstreamProductPurchased() {
        return () -> SinkConfig.FLUX_PRODUCT_PURCHASED;
    }

    @Bean
    public Supplier<Flux<Message<ProductReturned>>> downstreamProductReturned() {
        return () -> SinkConfig.FLUX_PRODUCT_RETURNED;
    }

//    @Bean
//    public Supplier<Flux<Message<?>>> downstreamWowOutput() {
//        return () -> SinkConfig.FLUX_WOW;
//    }

    @Configuration
    static class SinkConfig {

        // output sink #1 for check inventory
        private static final Sinks.Many<Message<InventoryCheckInitiated>>
                SINK_CHECK_INVENTORY = createSink(new InventoryCheckInitiated());

        private static final Flux<Message<InventoryCheckInitiated>>
                FLUX_CHECK_INVENTORY = createFlux(SINK_CHECK_INVENTORY);

        // output sink #2 for verify payment
        private static final Sinks.Many<Message<PaymentVerificationInitiated>>
                SINK_VERIFY_PAYMENT= createSink(new PaymentVerificationInitiated());

        private static final Flux<Message<PaymentVerificationInitiated>>
                FLUX_VERIFY_PAYMENT = createFlux(SINK_VERIFY_PAYMENT);

        // output sink #3 for process shipping
        private static final Sinks.Many<Message<ShippingInitiated>>
                SINK_SHIPPING= createSink(new ShippingInitiated());

        private static final Flux<Message<ShippingInitiated>>
                FLUX_SHIPPING = createFlux(SINK_SHIPPING);

        // output sink #4 for verify payment
        private static final Sinks.Many<Message<NewNotification>>
                SINK_NOTIFY_NEW_NOTIFICATION= createSink(new NewNotification());

        private static final Flux<Message<NewNotification>>
                FLUX_NOTIFY_NEW_NOTIFICATION = createFlux(SINK_NOTIFY_NEW_NOTIFICATION);


        // output sink #6 for product purchased
        private static final Sinks.Many<Message<ProductPurchased>>
                SINK_PRODUCT_PURCHASED= createSink(new ProductPurchased());

        private static final Flux<Message<ProductPurchased>>
                FLUX_PRODUCT_PURCHASED = createFlux(SINK_PRODUCT_PURCHASED);


        // output sink #7 for product returned
        private static final Sinks.Many<Message<ProductReturned>>
                SINK_PRODUCT_RETURNED= createSink(new ProductReturned());

        private static final Flux<Message<ProductReturned>>
                FLUX_PRODUCT_RETURNED = createFlux(SINK_PRODUCT_RETURNED);


        @Bean(name = "sinkCheckInventoryDownstream")
        Sinks.Many<Message<InventoryCheckInitiated>> sinkCheckInventoryDownstream() {
            return SINK_CHECK_INVENTORY;
        }

        @Bean(name = "sinkVerifyPaymentDownstream")
        Sinks.Many<Message<PaymentVerificationInitiated>> sinkVerifyPaymentDownstream() {
            return SINK_VERIFY_PAYMENT;
        }

        @Bean(name = "sinkShippingDownstream")
        Sinks.Many<Message<ShippingInitiated>> sinkShippingDownstream() {
            return SINK_SHIPPING;
        }

        @Bean(name = "sinkNewNotificationDownstream")
        Sinks.Many<Message<NewNotification>> sinkNewNotificationDownstream() {
            return SINK_NOTIFY_NEW_NOTIFICATION;
        }


        @Bean(name = "sinkProductPurchasedDownstream")
        Sinks.Many<Message<ProductPurchased>> sinkProductPurchasedDownstream() {
            return SINK_PRODUCT_PURCHASED;
        }


        @Bean(name = "sinkProductReturnedDownstream")
        Sinks.Many<Message<ProductReturned>> sinkProductReturnedDownstream() {
            return SINK_PRODUCT_RETURNED;
        }

    }

    private static <T>  Sinks.Many<Message<T>> createSink(T elem) {
        Sinks.Many<Message<T>> messageMany = Sinks.many().unicast().onBackpressureBuffer();
        return messageMany;
    }

    private static <T> Flux<Message<T>> createFlux(Sinks.Many<Message<T>> sink) {
        return sink.asFlux();
    }
}
