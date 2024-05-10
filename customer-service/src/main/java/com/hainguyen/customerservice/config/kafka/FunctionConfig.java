package com.hainguyen.customerservice.config.kafka;

import com.hainguyen.customerservice.events.downstream.UserContactRequest;
import com.hainguyen.customerservice.events.upstream.UserContactResponse;
import com.hainguyen.customerservice.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import reactor.core.publisher.Flux;

import java.util.function.Function;

@Configuration
@RequiredArgsConstructor
@Slf4j
public class FunctionConfig {
    private final UserRepository userRepository;

    @Bean
    public Function<Flux<UserContactRequest>, Flux<UserContactResponse>> fetchUserContactInfo() {
        return flux -> flux
                .doOnNext(req -> log.info("Received request to fetch user contact info: " + req))
                .flatMap(req -> userRepository
                        .findById(req.getUserId())
                        .map(user -> UserContactResponse.builder()
                                .orderId(req.getOrderId())
                                .userId(user.getId())
                                .email(user.getEmail())
                                .build()
                        )
                )
                .doOnNext(res -> log.info("Returning user contact info: " + res))
                ;

    }
}
