package com.hainguyen.orderservice.service;

import com.hainguyen.orderservice.dto.request.OrderUpdateRequest;
import com.hainguyen.orderservice.dto.request.PlaceOrderRequest;
import com.hainguyen.orderservice.dto.response.CommonResponse;
import com.hainguyen.orderservice.entity.Order;
import com.hainguyen.orderservice.events.downstream.InventoryCheckInitiated;
import com.hainguyen.orderservice.events.downstream.PaymentVerificationInitiated;
import com.hainguyen.orderservice.events.upstream.ProductPurchased;
import com.hainguyen.orderservice.events.downstream.ShippingInitiated;
import com.hainguyen.orderservice.events.upstream.InventoryChecked;
import com.hainguyen.orderservice.events.upstream.PaymentVerified;
import com.hainguyen.orderservice.events.upstream.ShippingStatusUpdated;
import com.hainguyen.orderservice.repository.OrderRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;
import reactor.core.publisher.Sinks;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import static com.hainguyen.orderservice.entity.Order.*;
import static com.hainguyen.orderservice.entity.Order.OrderStatus.*;

@Service
@AllArgsConstructor
@Slf4j
public class OrderService {
    private final Sinks.Many<Message<InventoryCheckInitiated>> sinkCheckInventoryDownstream;
    private final Sinks.Many<Message<PaymentVerificationInitiated>> sinkVerifyPaymentDownstream;
    private final Sinks.Many<Message<ShippingInitiated>> sinkShippingDownstream;
    private final Sinks.Many<Message<ProductPurchased>> sinkProductPurchasedDownstream;
    private final OrderRepository orderRepository;


    public Mono<CommonResponse> placeOrder(PlaceOrderRequest request) {
        // the status of the order is set to PROCESSING_CHECK_INVENTORY when the order is placed
        return orderRepository
                .save(this.createOrder(request))
                .map(order -> {
                    log.info("Order saved: {}", order);
                    this.checkProductInInventory(order);
                    return order;
                })
                .map(order -> {
                    log.info("Order placed successfully: {}", order);
                    CommonResponse commonResponse = this.buildSuccessResponse("Order placed successfully!");
                    Map<String, Integer> data = new HashMap<String, Integer>();
                    data.put("orderId", order.getId());
                    commonResponse.setData(data);
                    return commonResponse;
                })

                .onErrorContinue((e, b) -> {
                    log.error("Error occurred while placing order: {}", e.getMessage());
                    this.buildFailedResponse("Order placement failed!");
                });

    }

    private Order createOrder(PlaceOrderRequest request) {
        Order SAVE = builder()
                .status(OrderStatus.PROCESSING_CHECK_INVENTORY)
                .productId(request.getProductId())
                .userId(request.getUserId())
                .quantity(request.getQuantity())
                .shippingAddress(request.getShippingAddress())
                .cardNumber(request.getCardNumber())
                .totalPrice(0.0)
                .build();
        log.info("Order created: {}", SAVE);
        return SAVE;
    }

    public void checkProductInInventory(Order order) {
        // process the order
        log.info("Processing order: {}", order);
        InventoryCheckInitiated inventoryCheck = InventoryCheckInitiated.builder()
                .orderId(order.getId())
                .productId(order.getProductId())
                .quantity(order.getQuantity())
                .build();
        Message<InventoryCheckInitiated> message = MessageBuilder.withPayload(inventoryCheck).build();
        Sinks.EmitResult emitResult = sinkCheckInventoryDownstream.tryEmitNext(message);
        log.info("Emit result: {}", emitResult);

    }

    public Mono<Order> processInventoryCheckResult(InventoryChecked inventoryChecked) {
        return orderRepository.findById(inventoryChecked.getOrderId())
                .map(order -> {
                    if (inventoryChecked.getInStock()) {
                        order.setTotalPrice(inventoryChecked.getTotalPrice());
                        return order;
                    } else {
                        order.setStatus(OrderStatus.ON_HOLD_OUT_OF_STOCK);
                        return order;
                    }
                })
                .flatMap(orderRepository::save)
                .doOnNext(order -> log.info("Order price updated: {}", order))
                .map(order -> {
                    log.info("Processing inventory check result for order: {}", order);
                    return order;
                });
    }

    public Mono<Order> processPaymentVerification(int orderId) {
        System.out.println("wowoww");

        Mono<Order> byId = orderRepository.findById(orderId);
        return orderRepository.findById(orderId)
                .map(order -> {
                    log.info("Processing payment for order QQQQ: {}", order);
                    order.setStatus(OrderStatus.PROCESSING_VERIFY_PAYMENT);
                    log.info("Processing payment for order: {}", order);
                    return order;
                })
                .flatMap(orderRepository::save)
                .doOnNext(order -> log.info("Payment processed for order: {}", order))
                .map(order -> {
                    PaymentVerificationInitiated paymentVerification = PaymentVerificationInitiated.builder()
                            .orderId(order.getId())
                            .amount(order.getTotalPrice())
                            .cardNumber(order.getCardNumber())
                            .build();
                    Message<PaymentVerificationInitiated> message = MessageBuilder.withPayload(paymentVerification).build();
                    Sinks.EmitResult emitResult = sinkVerifyPaymentDownstream.tryEmitNext(message);
                    log.info("Emit result: {}", emitResult);
                    return order;
                });


    }

    public Mono<Order> processPaymentVerificationResult(PaymentVerified paymentVerified) {
        return orderRepository.findById(paymentVerified.getOrderId())
                .map(order -> {
                    if (paymentVerified.getVerified()) {
                        order.setPaymentVerified(true);
                    } else {
                        order.setPaymentVerified(false);
                        order.setStatus(OrderStatus.ON_HOLD_PAYMENT_FAILED);
                    }
                    return order;
                })
                .flatMap(orderRepository::save)
                .doOnNext(order -> log.info("Payment verification result processed for order: {}", order));
    }

    public Mono<Order> processShipping(int orderId) {
        return orderRepository.findById(orderId)
                .map(order -> {
                    order.setStatus(OrderStatus.PROCESSING_SHIPPING);
                    return order;
                })
                .flatMap(orderRepository::save)
                .doOnNext(order -> log.info("Shipping processed for order: {}", order))
                .map(order -> {
                    ShippingInitiated shippingInitiated = ShippingInitiated.builder()
                            .orderId(order.getId())
                            .address(order.getShippingAddress())
                            .build();
                    Message<ShippingInitiated> message = MessageBuilder.withPayload(shippingInitiated).build();
                    Sinks.EmitResult emitResult = sinkShippingDownstream.tryEmitNext(message);
                    log.info("Emit result: {}", emitResult);
                    return order;
                });
    }

    public Mono<Order> updateOrderShippingStatus(ShippingStatusUpdated shippingStatusUpdated) {
        OrderStatus status = OrderStatus.fromString(shippingStatusUpdated.getStatus());
        if (status == null) {
            log.error("Invalid order status: {}", shippingStatusUpdated.getStatus());
            return Mono.error(new IllegalArgumentException("Invalid order shipping status"));
        }

        return orderRepository.findById(shippingStatusUpdated.getOrderId())
                .<Order>handle((order, sink) -> {

                    if (!isValidStatusToUpdate(order.getStatus(), status)) {
                        log.error("Invalid order status, current status: {}, new status: {}", order.getStatus(), status);
                        sink.error(new IllegalArgumentException("Invalid order shipping status"));
                        return;
                    }

                    if (order.isPaymentVerified()) {
                        order.setStatus(status);
                        sink.next(order);
                    } else {
                        log.error("Order payment not verified: {}", order);
                        sink.next(order);
                    }
                })
                .flatMap(orderRepository::save)
                .doOnNext(order -> log.info("Order status updated: {}", order));

//        else {
//            log.error("Invalid order status: {}", status);
//            return Mono.error(new IllegalArgumentException("Invalid order shipping status"));
//        }
    }

    private boolean isValidStatusToUpdate(OrderStatus currentStatus, OrderStatus newStatus) {
        // new status can not be before the current status
        if (currentStatus.equals(PENDING)) {
            return newStatus.equals(PROCESSING_CHECK_INVENTORY)
                    || newStatus.equals(ON_HOLD_OUT_OF_STOCK)
                    || newStatus.equals(ON_HOLD_PAYMENT_FAILED);
        }

        if (currentStatus.equals(PROCESSING_CHECK_INVENTORY)) {
            return newStatus.equals(ON_HOLD_OUT_OF_STOCK);
        }

        if (currentStatus.equals(PROCESSING_VERIFY_PAYMENT)) {
            return newStatus.equals(ON_HOLD_PAYMENT_FAILED);
        }

        if (currentStatus.equals(PROCESSING_SHIPPING)) {
            return newStatus.equals(ON_HOLD_SHIPPING)
                    || newStatus.equals(SHIPPED);
        }

        if (currentStatus.equals(SHIPPED)) {
            return newStatus.equals(DELIVERED) || newStatus.equals(RETURNED);
        }

        return false;
    }


    private CommonResponse buildSuccessResponse(String message) {
        return CommonResponse.builder()
                .status(200)
                .message(message)
                .build();
    }

    private CommonResponse buildFailedResponse(String message) {
        return CommonResponse.builder()
                .status(400)
                .message(message)
                .build();
    }

    public Mono<Void> updateInventory(Order order) {
        return Mono.just(order)
                .map(this::buildProductPurchased)
                .flatMap(productPurchased -> {
                    Message<ProductPurchased> message = MessageBuilder.withPayload(productPurchased).build();
                    log.info("Product purchased: {}", productPurchased);
                    Sinks.EmitResult emitResult = sinkProductPurchasedDownstream.tryEmitNext(message);
                    log.info("Emit result: {}", emitResult);
                    return Mono.empty();
                });
    }

    private ProductPurchased buildProductPurchased(Order order) {
        return ProductPurchased.builder()
                .orderId(order.getId())
                .productId(order.getProductId())
                .quantity(order.getQuantity())
                .build();
    }

    public Mono<CommonResponse> updateOrder(OrderUpdateRequest request) {
        return Mono.just(request)
                .flatMap(req -> orderRepository.findById(req.getOrderId()))
                .flatMap(order -> {
                            log.info("Updating order: {}", order);
                            //TODO: write logic here
                            boolean cardNumberEditable =
                                    order.getStatus().equals(OrderStatus.ON_HOLD_PAYMENT_FAILED)
                                            && request.getCardNumber() != null
                                            && !Objects.equals(order.getCardNumber(), request.getCardNumber());
                            boolean shippingAddressEditable =
                                    order.getStatus().equals(OrderStatus.ON_HOLD_SHIPPING)
                                            && request.getShippingAddress() != null
                                            && !Objects.equals(order.getShippingAddress(), request.getShippingAddress());


                            // if the payment got failed and the request contains a card number, update the card number to re-process the payment verification
                            if (cardNumberEditable) {
                                order.setCardNumber(request.getCardNumber());
                                return
                                        orderRepository.save(order)
                                                .flatMap(o -> processPaymentVerification(o.getId()));
                            }

                            // if the shipping got failed and the request contains a shipping address, update the shipping address to re-process the shipping
                            if (shippingAddressEditable) {
                                order.setShippingAddress(request.getShippingAddress());
                                return orderRepository.save(order)
                                        .flatMap(o -> processPaymentVerification(o.getId()));
                            }

                            log.error("Order cannot be updated: {}: cardNumberEditable: {}, shippingAddressEditable: {}", order, cardNumberEditable, shippingAddressEditable);
                            return Mono.error(new IllegalArgumentException("Order cannot be updated"));
                        }
                )
                .map(order -> {
                    log.info("Order updated: {}", order);
                    return this.buildSuccessResponse("Order updated successfully!");
                })
                .onErrorResume(e -> {
                    log.error("Error occurred while updating order: {}", e.getMessage());
                    return Mono.just(this.buildFailedResponse("Order update failed!"));
                });

    }
}
