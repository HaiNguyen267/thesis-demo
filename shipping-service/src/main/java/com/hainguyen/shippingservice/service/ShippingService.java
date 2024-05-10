package com.hainguyen.shippingservice.service;

import com.hainguyen.shippingservice.dto.request.OrderDeliveredRequest;
import com.hainguyen.shippingservice.dto.request.OrderReturnedRequest;
import com.hainguyen.shippingservice.dto.response.CommonResponse;
import com.hainguyen.shippingservice.enums.OrderStatus;
import com.hainguyen.shippingservice.events.downstream.ShippingInitiated;
import com.hainguyen.shippingservice.events.upstream.ShippingStatusUpdated;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;
import reactor.core.publisher.Sinks;

@Service
@Slf4j
@RequiredArgsConstructor
public class ShippingService {


    private final  Sinks.Many<Message<ShippingStatusUpdated>> sinkShippingStatusUpdatedDownstream;

    public Mono<ShippingStatusUpdated> processShippingStatus(ShippingInitiated shippingInitiated) {
        return Mono.just(shippingInitiated)
                .map(shippingStatus -> {
                    String address = shippingStatus.getAddress();
                    log.info("Shipping initiated for order: {}", shippingStatus.getOrderId());
                    if (address == null || address.isEmpty()) {
                        return ShippingStatusUpdated.builder()
                                .orderId(shippingStatus.getOrderId())
                                .status(OrderStatus.ON_HOLD_SHIPPING.name())
                                .message("Address is required")
                                .build();
                    } else {
                        return ShippingStatusUpdated.builder()
                                .orderId(shippingStatus.getOrderId())
                                .status(OrderStatus.SHIPPED.name() )
                                .message("Shipping is initiated")
                                .build();
                    }
                });
    }

    public Mono<CommonResponse> orderDelivered(OrderDeliveredRequest request) {
        String reason = "Order is delivered by the shipper";
        return updateOrderShippingStatus(request.getOrderId(),OrderStatus.DELIVERED, reason, "Order delivery is processing...");
    }

    public Mono<CommonResponse> orderReturned(OrderReturnedRequest request) {
        return updateOrderShippingStatus(request.getOrderId(), OrderStatus.RETURNED,  request.getReason(), "Order return is processing...");
    }


    private Mono<CommonResponse> updateOrderShippingStatus(int orderId, OrderStatus status, String reason, String message) {
        return Mono.just(orderId)
                .doOnNext(req -> {
                    log.info("Updating order id {} shipping status: {}",orderId,  status);
                    ShippingStatusUpdated shippingStatusUpdated = ShippingStatusUpdated.builder()
                            .orderId(orderId)
                            .status(status.name())
                            .message(reason)
                            .build();

                    Message<ShippingStatusUpdated> kafkaMessage = MessageBuilder
                            .withPayload(shippingStatusUpdated)
                            .build();

                    log.info("Sending : {}", kafkaMessage);
                    sinkShippingStatusUpdatedDownstream.tryEmitNext(kafkaMessage);
                })
                .then(Mono.just(CommonResponse.builder()
                        .status(200)
                        .message(message)
                        .build()));
    }


}
