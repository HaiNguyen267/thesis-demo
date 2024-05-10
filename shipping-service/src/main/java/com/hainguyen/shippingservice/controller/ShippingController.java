package com.hainguyen.shippingservice.controller;

import com.hainguyen.shippingservice.dto.request.OrderDeliveredRequest;
import com.hainguyen.shippingservice.dto.request.OrderReturnedRequest;
import com.hainguyen.shippingservice.dto.response.CommonResponse;
import com.hainguyen.shippingservice.service.ShippingService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/v1/shipping")
@RequiredArgsConstructor
public class ShippingController {

    private final ShippingService shippingService;

    @PostMapping("/order-delivered")
    public Mono<CommonResponse> orderDelivered(@RequestBody OrderDeliveredRequest request) {
        return shippingService.orderDelivered(request);
    }

    @PostMapping("/order-returned")
    public Mono<CommonResponse> orderReturned(@RequestBody OrderReturnedRequest request) {
        return shippingService.orderReturned(request);
    }
}
