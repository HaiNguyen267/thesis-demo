package com.hainguyen.customerservice.functions;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import reactor.core.publisher.Mono;

import java.util.function.Function;

@Configuration
public class AppFunctions {

    @Bean
    public Function<Mono<Integer>, Mono<Integer>> function1() {
        return mono -> mono.map(i -> i * i);
    }
}
