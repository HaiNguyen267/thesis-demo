package com.hainguyen.paymentservice.config.kafka;

import org.springframework.context.annotation.Configuration;

@Configuration
public class ProducerBindingConfig {
//    @Bean
//    public Supplier<Flux<Message<InventoryCheckInitiated>>> downstreamCheckInventoryOutput() {
//        return () -> SinkConfig.FLUX_CHECK_INVENTORY;
//    }
//
//    @Bean
//    public Supplier<Flux<Message<PaymentVerificationInitiated>>> downstreamVerifyPaymentOutput() {
//        return () -> SinkConfig.FLUX_VERIFY_PAYMENT;
//    }
//
//    @Bean
//    public Supplier<Flux<Message<ShippingInitiated>>> downstreamShippingOutput() {
//        return () -> SinkConfig.FLUX_SHIPPING;
//    }

//    @Bean
//    public Supplier<Flux<Message<?>>> downstreamWowOutput() {
//        return () -> SinkConfig.FLUX_WOW;
//    }

//    @Configuration
//    static class SinkConfig {
//
//        // output sink #1 for check inventory
//        private static final Sinks.Many<Message<InventoryCheckInitiated>>
//                SINK_CHECK_INVENTORY = createSink(new InventoryCheckInitiated());
//
//        private static final Flux<Message<InventoryCheckInitiated>>
//                FLUX_CHECK_INVENTORY = createFlux(SINK_CHECK_INVENTORY);
//
//        // output sink #2 for verify payment
//        private static final Sinks.Many<Message<PaymentVerificationInitiated>>
//                SINK_VERIFY_PAYMENT= createSink(new PaymentVerificationInitiated());
//
//        private static final Flux<Message<PaymentVerificationInitiated>>
//                FLUX_VERIFY_PAYMENT = createFlux(SINK_VERIFY_PAYMENT);
//
//        // output sink #3 for process shipping
//        private static final Sinks.Many<Message<ShippingInitiated>>
//                SINK_SHIPPING= createSink(new ShippingInitiated());
//
//        private static final Flux<Message<ShippingInitiated>>
//                FLUX_SHIPPING = createFlux(SINK_SHIPPING);
//
//
//        @Bean(name = "sinkCheckInventoryDownstream")
//        Sinks.Many<Message<InventoryCheckInitiated>> sinkCheckInventoryDownstream() {
//            return SINK_CHECK_INVENTORY;
//        }
//
//        @Bean(name = "sinkVerifyPaymentDownstream")
//        Sinks.Many<Message<PaymentVerificationInitiated>> sinkVerifyPaymentDownstream() {
//            return SINK_VERIFY_PAYMENT;
//        }
//
//        @Bean(name = "sinkShippingDownstream")
//        Sinks.Many<Message<ShippingInitiated>> sinkShippingDownstream() {
//            return SINK_SHIPPING;
//        }
//
//
//    }
//
//    private static <T>  Sinks.Many<Message<T>> createSink(T elem) {
//        Sinks.Many<Message<T>> messageMany = Sinks.many().unicast().onBackpressureBuffer();
//        return messageMany;
//    }
//
//    private static <T> Flux<Message<T>> createFlux(Sinks.Many<Message<T>> sink) {
//        return sink.asFlux();
//    }
}
