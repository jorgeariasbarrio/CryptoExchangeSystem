package com.order.order_service.kafka;

import com.order.order_service.model.Order;
import com.order.order_service.service.OrderService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class OrderCompletedConsumer {

    OrderService orderService;
    @KafkaListener(topics = "order-completed", groupId = "matching-engine")
    public void consumeOrder(Order order) {
        log.info("Received completed order: {}", order);
        orderService.completedOrder(order);
    }
}
