package com.hainguyen.inventoryservice.config.kafka;

import com.hainguyen.inventoryservice.events.upstream.InventoryCheckResponse;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.Message;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Sinks;

import java.util.function.Supplier;

@Configuration
public class ProducerBindingConfig {
    @Bean
    public Supplier<Flux<Message<InventoryCheckResponse>>> upstreamCheckInventoryOutput() {
        return () -> SinkConfig.FLUX_CHECK_INVENTORY;
    }

//    @Bean
//    public Supplier<Flux<Message<?>>> downstreamWowOutput() {
//        return () -> SinkConfig.FLUX_WOW;
//    }

    @Configuration
    static class SinkConfig {

        // output sink #1
        private static final Sinks.Many<Message<InventoryCheckResponse>>
                SINK_CHECK_INVENTORY = createSink(new InventoryCheckResponse());

        private static final Flux<Message<InventoryCheckResponse>>
                FLUX_CHECK_INVENTORY = createFlux(SINK_CHECK_INVENTORY);


        // output sink #2
//        private static final Sinks.Many<Message<?>>
//                SINK_WOW = createSink();
//
//        private static final Flux<Message<?>>
//                FLUX_WOW = createFlux(SINK_WOW);

        // this output sink is used to send DispatcherEvent to ES
        @Bean(name = "sinkCheckInventoryUpstream")
        Sinks.Many<Message<InventoryCheckResponse>> sinkCheckInventoryUpstream() {
            return SINK_CHECK_INVENTORY;
        }

        // this output sink is used to send DispatcherEvent to DG
//        @Bean(name = "wowOutputSink")
//        Sinks.Many<Message<?>> pairingOutputSink() {
//            return SINK_WOW;
//        }
    }

    private static <T>  Sinks.Many<Message<T>> createSink(T elem) {
        Sinks.Many<Message<T>> messageMany = Sinks.many().unicast().onBackpressureBuffer();
        return messageMany;
    }

    private static <T> Flux<Message<T>> createFlux(Sinks.Many<Message<T>> sink) {
        return sink.asFlux();
    }
}
