package com.hainguyen.orderservice.config;

import com.hainguyen.orderservice.config.converter.OrderStatusReadingConverter;
import com.hainguyen.orderservice.config.converter.OrderStatusWritingConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.r2dbc.config.EnableR2dbcAuditing;
import org.springframework.data.r2dbc.convert.MappingR2dbcConverter;
import org.springframework.data.r2dbc.convert.R2dbcCustomConversions;
import org.springframework.data.r2dbc.dialect.DialectResolver;
import org.springframework.data.r2dbc.mapping.R2dbcMappingContext;
import org.springframework.data.relational.core.mapping.NamingStrategy;

import java.util.Arrays;

@Configuration
public class R2dbcConfig {

//    @Bean
//    public R2dbcCustomConversions r2dbcCustomConversions() {
//        return new R2dbcCustomConversions(Arrays.asList(
//                new OrderStatusReadingConverter(),
//                new OrderStatusWritingConverter()
//        ));
//    }
}