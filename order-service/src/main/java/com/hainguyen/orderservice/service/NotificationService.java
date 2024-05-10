package com.hainguyen.orderservice.service;

import com.hainguyen.orderservice.entity.Order;
import com.hainguyen.orderservice.events.downstream.NewNotification;
import com.hainguyen.orderservice.events.downstream.OrderShippingStatusChanged;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;
import reactor.core.publisher.Sinks;

@Service
@RequiredArgsConstructor
@Slf4j
public class NotificationService {
    private final Sinks.Many<Message<NewNotification>> sinkNotifyOrderOnHoldDownstream;
//    private final Sinks.Many<Message<OrderShippingStatusChanged>> sinkNotifyShippingStatusChangedDownstream;


    public Mono<Void> sendNotificationOrderOnHold(Order order) {
        return Mono.just(order)
                .map(this::createNewNotificationEvent)
                .doOnNext(orderOnHold -> {
                    // send notification to customer when the order is on hold
                    Message<NewNotification> message = MessageBuilder.withPayload(orderOnHold).build();
                    sinkNotifyOrderOnHoldDownstream.tryEmitNext(message);
                })
                // return Mono<Void>
                .then(Mono.empty());
    }

    private NewNotification createNewNotificationEvent(Order order) {
        return NewNotification.builder()
                .orderId(order.getId())
                .userId(order.getUserId())
                .status(order.getStatus().name())
                .timestamp(String.valueOf(System.currentTimeMillis()))
                .build();
    }

    public Mono<Void> sendNotificationOrderShippingStatus(Order order) {
        return Mono.just(order)
                .doOnNext(o -> log.info("Sending notification for order: {}", o))
                .map(this::createNewNotificationEvent)
                .doOnNext(orderShippingStatusChanged -> {
                    // send notification to customer when the order is shipped
                    log.info("Sending notification for order: {}", orderShippingStatusChanged);
                    Message<NewNotification> message = MessageBuilder.withPayload(orderShippingStatusChanged).build();
                    sinkNotifyOrderOnHoldDownstream.tryEmitNext(message);
                })
                // return Mono<Void>
                .then(Mono.empty())
        ;
    }


}
