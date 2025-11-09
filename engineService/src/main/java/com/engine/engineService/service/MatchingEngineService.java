package com.engine.engineService.service;

import com.engine.engineService.client.PortfolioClient;
import com.engine.engineService.core.OrderBook;
import com.engine.engineService.domain.Order;
import com.engine.engineService.domain.Trade;
import com.engine.engineService.kafka.OrderProducer;
import com.engine.engineService.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.locks.ReentrantLock;

@Service
@RequiredArgsConstructor
@Slf4j
public class MatchingEngineService {

    private final OrderRepository orderRepository;
    private final OrderBookService orderBookService;

    private final Map<String, ReentrantLock> locks = new HashMap<>();

    public Optional<Trade> processOrder(Order order) {
        ReentrantLock lock = locks.computeIfAbsent(order.getAsset(), k -> new ReentrantLock());
        lock.lock();
        try {
            orderRepository.save(order);
            Optional<Trade> trade = orderBookService.match(order);
            trade.ifPresent(t -> log.info("Matched successfully: {}", t));
            return trade;
        } finally {
            lock.unlock();
        }
    }
}
