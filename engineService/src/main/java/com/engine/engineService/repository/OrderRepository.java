package com.engine.engineService.repository;

import com.engine.engineService.domain.Order;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface OrderRepository extends MongoRepository<Order, String> {
    List<Order> findByStatus(String status);
}
