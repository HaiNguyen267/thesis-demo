package com.hainguyen.orderservice.repository;

import com.hainguyen.orderservice.entity.Order;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderRepository extends R2dbcRepository<Order, Integer> {

}
