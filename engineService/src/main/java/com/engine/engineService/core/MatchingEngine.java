package com.engine.engineService.core;

import com.engine.engineService.client.PortfolioClient;
import com.engine.engineService.domain.Order;
import com.engine.engineService.domain.Trade;
import com.engine.engineService.kafka.OrderProducer;
import com.engine.engineService.kafka.TradeProducer;
import com.engine.engineService.service.OrderBookService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.locks.ReentrantLock;

@Component
@RequiredArgsConstructor
@Slf4j
public class MatchingEngine {

    private final OrderBookService orderBookService;
    private final ConcurrentHashMap<String, ReentrantLock> locks = new ConcurrentHashMap<>();

    public Optional<Trade> processOrder(Order order) {
        ReentrantLock lock = locks.computeIfAbsent(order.getAsset(), k -> new ReentrantLock());
        lock.lock();
        try {
            return orderBookService.match(order);
        } finally {
            lock.unlock();
        }
    }
}
