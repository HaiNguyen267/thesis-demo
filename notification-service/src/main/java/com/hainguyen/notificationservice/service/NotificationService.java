package com.hainguyen.notificationservice.service;

import com.hainguyen.notificationservice.events.upstream.UserContactResponse;
import com.hainguyen.notificationservice.model.Mail;
import com.hainguyen.notificationservice.model.Notification;
import com.hainguyen.notificationservice.model.OrderStatus;
import lombok.RequiredArgsConstructor;
import org.reactivestreams.Publisher;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.concurrent.ConcurrentHashMap;

import static com.hainguyen.notificationservice.service.MailService.constructMailContent;
import static com.hainguyen.notificationservice.service.MailService.constructMailSubject;

@Service
@RequiredArgsConstructor
public class NotificationService {
    private ConcurrentHashMap<String, Notification> map = new ConcurrentHashMap<>();
    private final MailService mailService;

    public Notification constructNotification(int userId, int orderId, OrderStatus orderStatus) {
        String mailContent = constructMailContent(orderStatus);
        String mailSubject = constructMailSubject(orderStatus);
        Mail mail = Mail.builder()
                .content(mailContent)
                .subject(mailSubject)
                .build();

        return Notification.builder()
                .userId(userId)
                .orderId(orderId)
                .mail(mail)
                .build();
    }


    public void addNotification(Notification notification) {
        String key = getNotificationKey(notification.getUserId(), notification.getOrderId());
        map.put(key, notification);
    }

    public Notification getNotification(int userId, int orderId) {
        String key = getNotificationKey(userId, orderId);
        return map.get(key);
    }

    public Notification getNotification(String key) {
        return map.get(key);
    }

    public void updateEmailAddressForNotification(int userId, int orderId, String email) {
        String key = getNotificationKey(userId, orderId);
        updateEmailAddressForNotification(key, email);
    }

    public void updateEmailAddressForNotification(String key, String email) {
        Notification notification = map.get(key);
        if (notification != null) {
            Mail mail = notification.getMail();
            if (mail != null) {
                mail.setRecipient(email);
            }
        }
    }

    public String getNotificationKey(int userId, int orderId) {
        return userId + "/" + orderId;
    }

    public Mono<Void> sendMail(UserContactResponse userContactResponse) {
        String key = getNotificationKey(userContactResponse.getUserId(), userContactResponse.getOrderId());
        String emailAddress = userContactResponse.getEmail();

        Notification notification = getNotification(key);


        return Mono.just(notification)
                .flatMap(notification1 -> {
                    Mail mail = notification1.getMail();
                    if (mail != null) {
                        mail.setRecipient(emailAddress);
                        return mailService.sendMail(mail);
                    }
                    return Mono.empty();
                });

    }
}
