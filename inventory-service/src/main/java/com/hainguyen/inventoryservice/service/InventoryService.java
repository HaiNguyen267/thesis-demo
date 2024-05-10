package com.hainguyen.inventoryservice.service;

import com.hainguyen.inventoryservice.events.downstream.InventoryCheck;
import com.hainguyen.inventoryservice.events.downstream.ProductPurchased;
import com.hainguyen.inventoryservice.events.upstream.InventoryCheckResponse;
import com.hainguyen.inventoryservice.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import static java.lang.Boolean.FALSE;
import static java.lang.Boolean.TRUE;

@Service
@RequiredArgsConstructor
public class InventoryService {

    private final ProductRepository productRepository;

    public Mono<InventoryCheckResponse> checkInventory(InventoryCheck inventoryCheck) {

        // TODO: check if the product in the inventory with the given quantity available
        return Mono.just(inventoryCheck)
                .flatMap(request -> productRepository.findById(request.getProductId()))
                .flatMap(product -> {
                    if (product.getQuantity() >= inventoryCheck.getQuantity()) {
                        return Mono.just(InventoryCheckResponse.builder()
                                .orderId(inventoryCheck.getOrderId())
                                .productId(inventoryCheck.getProductId())
                                .inStock(TRUE)
                                .totalPrice(product.getPrice() * inventoryCheck.getQuantity())
                                .build());
                    } else {
                        return Mono.just(InventoryCheckResponse.builder()
                                .orderId(inventoryCheck.getOrderId())
                                .productId(inventoryCheck.getProductId())
                                .inStock(FALSE)
                                .build());
                    }
                }
        );
    }


    public Mono<Void> updateProductQuantity(int productId, int quantity, boolean isReturned) {
       return Mono.just(productId)
               .flatMap(productRepository::findById)
               .flatMap(product -> {
                   if (isReturned) {
                       product.setQuantity(product.getQuantity() + quantity);
                   } else {
                       product.setQuantity(product.getQuantity() - quantity);
                   }
                   return productRepository.save(product);
               })
               .then(Mono.empty());
    }
}
