package com.hainguyen.shippingservice.config.kafka;

import com.hainguyen.shippingservice.events.downstream.ShippingInitiated;
import com.hainguyen.shippingservice.events.upstream.ShippingStatusUpdated;
import com.hainguyen.shippingservice.service.ShippingService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import reactor.core.publisher.Flux;

import java.util.function.Function;

@Configuration
@RequiredArgsConstructor
@Log4j2
public class ConsumerBindingConfig {

    private final ShippingService shippingService;


    @Bean
    public Function<Flux<ShippingInitiated>, Flux<ShippingStatusUpdated>> processingShippingFunction() {
        return flux -> flux
                .doOnNext(shippingRequest -> log.info("Received order to ship: {}", shippingRequest))
                .flatMap(shippingService::processShippingStatus);
    }



}
