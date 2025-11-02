package com.engine.engineService.core;

import com.engine.engineService.domain.Order;
import com.engine.engineService.domain.Trade;
import com.engine.engineService.service.OrderBookService;

import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.locks.ReentrantLock;

public class MatchingEngine {

    private final OrderBookService orderBookService;
    private final ConcurrentHashMap<String, ReentrantLock> locks = new ConcurrentHashMap<>();

    public MatchingEngine(OrderBookService orderBookService) {
        this.orderBookService = orderBookService;
    }

    public Optional<Trade> processOrder(Order order) {
        ReentrantLock lock = locks.computeIfAbsent(order.getAsset(), k -> new ReentrantLock());
        lock.lock();
        try {
            // Aquí delegas la lógica del matching
            return orderBookService.match(order);
        } finally {
            lock.unlock();
        }
    }
}
