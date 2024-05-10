package com.hainguyen.notificationservice.config.kafka;

import com.hainguyen.notificationservice.events.downstream.UserContactRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.Message;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Sinks;

import java.util.function.Supplier;

@Configuration
public class ProducerBindingConfig {
    @Bean
    public Supplier<Flux<Message<UserContactRequest>>> downstreamUserContactRequest() {
        return () -> SinkConfig.FLUX_USER_CONTACT_REQUEST;
    }


    @Configuration
    static class SinkConfig {

        // output sink #1 for check inventory
        private static final Sinks.Many<Message<UserContactRequest>>
                SINK_USER_CONTACT_REQUEST = createSink(new UserContactRequest());

        private static final Flux<Message<UserContactRequest>>
                FLUX_USER_CONTACT_REQUEST = createFlux(SINK_USER_CONTACT_REQUEST);


        @Bean(name = "sinkUserContactRequestDownstream")
        Sinks.Many<Message<UserContactRequest>> sinkUserContactRequestDownstream() {
            return SINK_USER_CONTACT_REQUEST;
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
