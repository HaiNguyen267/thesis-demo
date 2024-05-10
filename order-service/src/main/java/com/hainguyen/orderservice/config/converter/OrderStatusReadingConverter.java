package com.hainguyen.orderservice.config.converter;

import com.hainguyen.orderservice.entity.Order;
import io.r2dbc.spi.Row;
import org.springframework.core.convert.converter.Converter;
import org.springframework.data.convert.ReadingConverter;

import java.time.LocalDateTime;

@ReadingConverter
public class OrderStatusReadingConverter implements Converter<Row, Order> {

    public Order convert(Row source) {
        return
        Order.builder()
                .id(source.get("id", Integer.class))
                .status(Order.OrderStatus.valueOf(source.get("status", String.class).toUpperCase()))
                .userId(source.get("customer_id", Integer.class))
                .productId(source.get("product_id", Integer.class))
                .quantity(source.get("quantity", Integer.class))
                .totalPrice(source.get("total_price", Double.class))
                .createdAt(source.get("created_at", LocalDateTime.class))
                .updatedAt(source.get("updated_at", LocalDateTime.class))
                .build();
    }
}