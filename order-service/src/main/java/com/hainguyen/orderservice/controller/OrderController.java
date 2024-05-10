package com.hainguyen.orderservice.controller;

import com.hainguyen.orderservice.dto.request.OrderUpdateRequest;
import com.hainguyen.orderservice.dto.request.PlaceOrderRequest;
import com.hainguyen.orderservice.dto.response.CommonResponse;
import com.hainguyen.orderservice.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RequiredArgsConstructor
@RestController()
@RequestMapping("/api/v1/order")
public class OrderController {
    private final OrderService orderService;


    @PostMapping()
    public Mono<CommonResponse> placeOrder(@RequestBody PlaceOrderRequest request) {
        return orderService.placeOrder(request);
    }

    @PostMapping("/update")
    public Mono<CommonResponse> updateOrder(@RequestBody OrderUpdateRequest request) {
        return orderService.updateOrder(request);
    }

}
