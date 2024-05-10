package com.hainguyen.orderservice.config.converter;

import com.hainguyen.orderservice.entity.Order;
import io.r2dbc.spi.Row;
import org.springframework.core.convert.converter.Converter;
import org.springframework.data.convert.WritingConverter;
import org.springframework.data.r2dbc.mapping.OutboundRow;
import org.springframework.r2dbc.core.Parameter;

@WritingConverter
public class OrderStatusWritingConverter implements Converter<Order, OutboundRow> {

    @Override
    public OutboundRow convert(Order source) {
        OutboundRow row = new OutboundRow();
        row.put("id", Parameter.from(source.getId()));
        row.put("status", Parameter.from(source.getStatus().name()));
        row.put("customer_id", Parameter.from(source.getUserId()));
        row.put("product_id", Parameter.from(source.getProductId()));
        row.put("quantity", Parameter.from(source.getQuantity()));
        row.put("total_price", Parameter.from(source.getTotalPrice()));
        row.put("created_at", Parameter.from(source.getCreatedAt()));
        row.put("updated_at", Parameter.from(source.getUpdatedAt()));
        return row;
    }
}