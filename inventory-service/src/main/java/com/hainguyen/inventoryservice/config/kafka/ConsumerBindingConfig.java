package com.hainguyen.inventoryservice.config.kafka;

import com.hainguyen.inventoryservice.events.downstream.InventoryCheck;
import com.hainguyen.inventoryservice.events.downstream.ProductPurchased;
import com.hainguyen.inventoryservice.events.downstream.ProductReturned;
import com.hainguyen.inventoryservice.events.upstream.InventoryCheckResponse;
import com.hainguyen.inventoryservice.service.InventoryService;
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
    private final InventoryService inventoryService;

    @Bean
    public Function<Flux<InventoryCheck>, Flux<InventoryCheckResponse>> downstreamCheckInventoryFunction() {
        return flux -> flux
                .doOnNext(inventoryCheck -> log.info("Received inventory check request: {}", inventoryCheck))
                .flatMap(inventoryService::checkInventory)
                .onErrorResume(e -> {
                    log.error("Error occurred while checking inventory: {}", e.getMessage());
                    return Flux.empty();
                });

    }

    @Bean
    public Consumer<Flux<ProductPurchased>> downstreamProductPurchasedFunction() {
        return flux -> flux
                .doOnNext(productPurchased -> log.info("Received product purchased event: {}", productPurchased))
                .flatMap(productPurchased -> inventoryService.updateProductQuantity(productPurchased.getProductId(), productPurchased.getQuantity(), false))
                .onErrorResume(e -> {
                    log.error("Error occurred while updating inventory: {}", e.getMessage());
                    return Flux.empty();
                })
                .subscribe();
    }

    @Bean
    public Consumer<Flux<ProductReturned>> downstreamProductReturnedFunction() {
        return flux -> flux
                .doOnNext(productPurchased -> log.info("Received product purchased event: {}", productPurchased))
                .flatMap(productPurchased -> inventoryService.updateProductQuantity(productPurchased.getProductId(), productPurchased.getQuantity(), true))
                .onErrorResume(e -> {
                    log.error("Error occurred while updating inventory: {}", e.getMessage());
                    return Flux.empty();
                })
                .subscribe();
    }


}
