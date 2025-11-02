package com.engine.engineService.core;

import com.engine.engineService.domain.Order;
import com.engine.engineService.domain.OrderType;
import com.engine.engineService.kafka.OrderProducer;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Comparator;
import java.util.PriorityQueue;

@Component
@Slf4j
@RequiredArgsConstructor
public class OrderBook {

    private OrderProducer orderProducer;

    private final PriorityQueue<Order> buyOrders =
            new PriorityQueue<>(Comparator.comparingDouble(Order::getPrice).reversed()); // always highest first

    private final PriorityQueue<Order> sellOrders =
            new PriorityQueue<>(Comparator.comparingDouble(Order::getPrice));// always lowest first

    public void match(Order incoming) {
        if (incoming.getType() == OrderType.BUY) {
            matchBuy(incoming);
        } else {
            matchSell(incoming);
        }
    }

    private void matchBuy(Order buyOrder) {
        while (!sellOrders.isEmpty() &&
                buyOrder.getQuantity() > 0 &&
                buyOrder.getPrice() >= sellOrders.peek().getPrice()) {

            Order sellOrder = sellOrders.peek();
            double tradedQty = Math.min(buyOrder.getQuantity(), sellOrder.getQuantity());
            double tradePrice = sellOrder.getPrice(); // el precio del vendedor manda

            log.info("TRADE EXECUTED: BUY {} vs SELL {} @ {} qty {}",
                    buyOrder.getId(), sellOrder.getId(), tradePrice, tradedQty);


            buyOrder.fill(tradedQty);
            sellOrder.fill(tradedQty);

            if (sellOrder.isFilled()) {
                sellOrders.poll(); // eliminar del libro si se completó
            }

        }

        if (!buyOrder.isFilled()) {
            buyOrders.add(buyOrder);
        }
        else {
            orderProducer.sendOrder("order-completed", buyOrder);
        }
    }

    private void matchSell(Order sellOrder) {
        while (!buyOrders.isEmpty() &&
                sellOrder.getQuantity() > 0 &&
                sellOrder.getPrice() <= buyOrders.peek().getPrice()) {

            Order buyOrder = buyOrders.peek();
            double tradedQty = Math.min(sellOrder.getQuantity(), buyOrder.getQuantity());
            double tradePrice = buyOrder.getPrice();

            log.info("TRADE EXECUTED: SELL {} vs BUY {} @ {} qty {}",
                    sellOrder.getId(), buyOrder.getId(), tradePrice, tradedQty);

            sellOrder.fill(tradedQty);
            buyOrder.fill(tradedQty);

            if (buyOrder.isFilled()) {
                buyOrders.poll();
            }
        }

        if (!sellOrder.isFilled()) {
            sellOrders.add(sellOrder);
        }
        else {
            orderProducer.sendOrder("order-completed", sellOrder);
        }
    }

    public Order bestBid() {
        return buyOrders.peek();
    }

    public Order bestAsk() {
        return sellOrders.peek();
    }
}

