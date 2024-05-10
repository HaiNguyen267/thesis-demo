package com.hainguyen.shippingservice.config.kafka;

import com.hainguyen.shippingservice.events.downstream.ProductPurchased;
import com.hainguyen.shippingservice.events.downstream.ShippingInitiated;
import com.hainguyen.shippingservice.events.upstream.ShippingStatusUpdated;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.Message;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Sinks;

import java.util.function.Supplier;

@Configuration
public class ProducerBindingConfig {

    @Bean
    public Supplier<Flux<Message<ShippingStatusUpdated>>> downstreamShippingOutput() {
        return () -> SinkConfig.FLUX_SHIPPING_STATUS_UPDATED;
    }

    @Configuration
    static class SinkConfig {

        // output sink #1 for update shipping status
        private static final Sinks.Many<Message<ShippingStatusUpdated>>
                SINK_SHIPPING_STATUS_UPDATED = createSink(new ShippingStatusUpdated());

        private static final Flux<Message<ShippingStatusUpdated>>
                FLUX_SHIPPING_STATUS_UPDATED = createFlux(SINK_SHIPPING_STATUS_UPDATED);


        @Bean(name = "sinkShippingStatusUpdatedDownstream")
        Sinks.Many<Message<ShippingStatusUpdated>> sinkShippingStatusUpdatedDownstream() {
            return SINK_SHIPPING_STATUS_UPDATED;
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
