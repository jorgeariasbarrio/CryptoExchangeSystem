package com.engine.engineService.core;

import com.engine.engineService.client.PortfolioClient;
import com.engine.engineService.domain.Order;
import com.engine.engineService.domain.OrderType;
import com.engine.engineService.domain.Trade;
import com.engine.engineService.kafka.OrderProducer;
import com.engine.engineService.model.PortfolioRequestDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Comparator;
import java.util.Optional;
import java.util.PriorityQueue;

@Component
@Slf4j
@RequiredArgsConstructor
public class OrderBook {

    private final PriorityQueue<Order> buyOrders =
            new PriorityQueue<>(Comparator.comparingDouble(Order::getPrice).reversed()); // highest first
    private final PriorityQueue<Order> sellOrders =
            new PriorityQueue<>(Comparator.comparingDouble(Order::getPrice)); // lowest first

    public Optional<Trade> match(Order incoming) {
        if (incoming.getType() == OrderType.BUY) {
            return matchBuy(incoming);
        } else {
            return matchSell(incoming);
        }
    }

    private Optional<Trade> matchBuy(Order buyOrder) {
        while (!sellOrders.isEmpty()
                && buyOrder.getQuantity() > 0
                && buyOrder.getPrice() >= sellOrders.peek().getPrice()) {

            Order sellOrder = sellOrders.peek();
            double qty = Math.min(buyOrder.getQuantity(), sellOrder.getQuantity());
            double price = sellOrder.getPrice();

            log.info("TRADE EXECUTED: BUY {} vs SELL {} @ {} qty {}",
                    buyOrder.getId(), sellOrder.getId(), price, qty);

            buyOrder.fill(qty);
            sellOrder.fill(qty);

            Trade trade = new Trade(buyOrder.getId(), sellOrder.getId(), sellOrder.getAsset(), price, qty);

            if (sellOrder.isFilled()) {
                sellOrders.poll();
            }
            return Optional.of(trade);
        }

        // No se macha, se guarda en el libro
        if (!buyOrder.isFilled()) {
            buyOrders.add(buyOrder);
        }

        return Optional.empty();
    }

    private Optional<Trade> matchSell(Order sellOrder) {
        while (!buyOrders.isEmpty()
                && sellOrder.getQuantity() > 0
                && sellOrder.getPrice() <= buyOrders.peek().getPrice()) {

            Order buyOrder = buyOrders.peek();
            double qty = Math.min(sellOrder.getQuantity(), buyOrder.getQuantity());
            double price = buyOrder.getPrice();

            log.info("TRADE EXECUTED: SELL {} vs BUY {} @ {} qty {}",
                    sellOrder.getId(), buyOrder.getId(), price, qty);

            sellOrder.fill(qty);
            buyOrder.fill(qty);

            Trade trade = new Trade(buyOrder.getId(), sellOrder.getId(), sellOrder.getAsset(), price, qty);

            if (buyOrder.isFilled()) {
                buyOrders.poll();
            }
            return Optional.of(trade);
        }

        if (!sellOrder.isFilled()) {
            sellOrders.add(sellOrder);
        }

        return Optional.empty();
    }
}

