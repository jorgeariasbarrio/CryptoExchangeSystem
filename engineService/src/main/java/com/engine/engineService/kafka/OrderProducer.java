package com.engine.engineService.kafka;

import com.engine.engineService.domain.Order;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class OrderProducer {
    private final KafkaTemplate<String, Order> kafkaTemplate;

    public void sendOrder(String topic, Order order) {
        kafkaTemplate.send(topic, order);
    }
}