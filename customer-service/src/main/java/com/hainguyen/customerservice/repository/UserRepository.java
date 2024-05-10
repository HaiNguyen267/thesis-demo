package com.hainguyen.customerservice.repository;

import com.hainguyen.customerservice.entity.User;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends R2dbcRepository<User, Integer> {
}
