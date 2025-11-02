package com.engine.engineService.service;

import com.engine.engineService.domain.Order;
import com.engine.engineService.domain.Trade;
import com.engine.engineService.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class MatchingEngineService {

    private final OrderRepository orderRepository;
    private final OrderBook match
    S
     ervice orderBookService;

    public void processOrder(Order newOrder) {
        orderRepository.save(newOrder);
        Optional<Trade> trade = orderBookService.match(newOrder);
        trade.ifPresent(t -> log.info("Matched: {}", t));
    }
}
