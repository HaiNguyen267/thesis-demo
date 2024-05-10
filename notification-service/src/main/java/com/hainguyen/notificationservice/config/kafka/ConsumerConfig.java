package com.hainguyen.notificationservice.config.kafka;

import com.hainguyen.notificationservice.events.upstream.UserContactResponse;
import com.hainguyen.notificationservice.model.Notification;
import com.hainguyen.notificationservice.events.downstream.UserContactRequest;
import com.hainguyen.notificationservice.events.upstream.NewNotificationEvent;
import com.hainguyen.notificationservice.events.upstream.OrderShippingStatusChanged;
import com.hainguyen.notificationservice.model.OrderStatus;
import com.hainguyen.notificationservice.service.MailService;
import com.hainguyen.notificationservice.service.NotificationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Sinks;

import java.util.function.Consumer;

@Configuration
@RequiredArgsConstructor
@Slf4j
public class ConsumerConfig {

    private final MailService mailService;
    private final NotificationService notificationService;

    private final  Sinks.Many<Message<UserContactRequest>> sinkUserContactRequestDownstream;

    @Bean
    public Consumer<Flux<NewNotificationEvent>> downstreamNewNotification() {
        return flux -> flux
                .doOnNext(response -> log.info("Received shipping status updated: {}", response))
                .flatMap(newNotification -> {
                    OrderStatus orderStatus = OrderStatus.fromString(newNotification.getStatus());
                    Notification notification = notificationService.constructNotification(newNotification.getUserId(), newNotification.getOrderId(), orderStatus);
                    notificationService.addNotification(notification);

                    UserContactRequest userContactRequest = UserContactRequest.builder()
                            .userId(newNotification.getUserId())
                            .orderId(newNotification.getOrderId())
                            .build();
                    sinkUserContactRequestDownstream.tryEmitNext(MessageBuilder.withPayload(userContactRequest).build());
                    return Flux.just(notification);
                })
                .subscribe();
    }

    @Bean
    public Consumer<Flux<UserContactResponse>> upstreamUserContact() {
        return flux -> flux
                .doOnNext(response -> log.info("Received shipping status updated: {}", response))
                .flatMap(res -> notificationService.sendMail(res))
                .subscribe();
    }

}
