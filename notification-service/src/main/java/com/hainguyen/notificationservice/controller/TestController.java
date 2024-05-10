package com.hainguyen.notificationservice.controller;

import com.hainguyen.notificationservice.service.MailService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class TestController {

    private final MailService mailService;

    @PostMapping("/")
    public String test() {
        return mailService.sendSimpleMail();
    }
}
