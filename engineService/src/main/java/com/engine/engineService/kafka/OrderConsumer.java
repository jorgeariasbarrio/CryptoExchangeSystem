package com.engine.engineService.kafka;

import com.engine.engineService.domain.Order;
import com.engine.engineService.service.MatchingEngineService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class OrderConsumer {

    private final MatchingEngineService matchingService;

    @KafkaListener(topics = "order.pending", groupId = "matching-engine")
    public void consumeOrder(Order order) {
        log.info("Received order: {}", order);
        matchingService.processOrder(order);
    }
}
