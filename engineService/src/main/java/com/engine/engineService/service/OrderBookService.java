package com.engine.engineService.service;

import com.engine.engineService.domain.Order;
import com.engine.engineService.domain.OrderType;
import com.engine.engineService.domain.Trade;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@Slf4j
public class OrderBookService {

    private final Map<Double, List<Order>> buyOrders = new TreeMap<>(Comparator.reverseOrder());
    private final Map<Double, List<Order>> sellOrders = new TreeMap<>();

    public synchronized Optional<Trade> match(Order order) {
        if (order.getType() == OrderType.BUY) {
            return matchBuy(order);
        } else {
            return matchSell(order);
        }
    }

    private Optional<Trade> matchBuy(Order buyOrder) {
        // Aquí va la lógica de matching BUY vs SELL
        return Optional.empty();
    }

    private Optional<Trade> matchSell(Order sellOrder) {
        // Aquí va la lógica de matching SELL vs BUY
        return Optional.empty();
    }
}

