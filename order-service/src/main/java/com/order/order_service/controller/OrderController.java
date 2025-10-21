package com.order.order_service.controller;

import com.order.order_service.model.Order;
import com.order.order_service.service.OrderService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/orders")
public class OrderController {

    private final OrderService service;

    public OrderController(OrderService service) {
        this.service = service;
    }

    @PostMapping("/createOrder")
    public Order createOrder(@RequestBody Order order) {
        return service.createOrder(order);
    }
}

