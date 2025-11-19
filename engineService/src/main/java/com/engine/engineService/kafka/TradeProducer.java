package com.engine.engineService.kafka;

import com.engine.engineService.domain.Trade;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class TradeProducer {
    private final KafkaTemplate<String, Trade> kafkaTemplate;

    public void sendOrder(String topic, Trade trade) {
        kafkaTemplate.send(topic, trade);
    }
}
