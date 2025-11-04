package com.engine.engineService.core;

import com.engine.engineService.client.PortfolioClient;
import com.engine.engineService.domain.Order;
import com.engine.engineService.domain.OrderType;
import com.engine.engineService.kafka.OrderProducer;
import com.engine.engineService.model.PortfolioRequestDto;
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

    private PortfolioClient portfolioClient;

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
            PortfolioRequestDto portfolioRequestDto = new PortfolioRequestDto(buyOrder.getUserId(),
                    buyOrder.getAsset(), buyOrder.getQuantity(), OrderType.BUY, buyOrder.getPrice());
            portfolioClient.updatePorfolio(portfolioRequestDto);
            sellOrder.fill(tradedQty);
            portfolioRequestDto = new PortfolioRequestDto(sellOrder.getUserId(),
                    sellOrder.getAsset(), sellOrder.getQuantity(), OrderType.SELL, sellOrder.getPrice());
            portfolioClient.updatePorfolio(portfolioRequestDto);
            if (sellOrder.isFilled()) {
                sellOrders.poll();
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
            PortfolioRequestDto portfolioRequestDto = new PortfolioRequestDto(sellOrder.getUserId(),
                    sellOrder.getAsset(), sellOrder.getQuantity(), OrderType.SELL, sellOrder.getPrice());
            portfolioClient.updatePorfolio(portfolioRequestDto);
            buyOrder.fill(tradedQty);
            portfolioRequestDto = new PortfolioRequestDto(buyOrder.getUserId(),
                    buyOrder.getAsset(), buyOrder.getQuantity(), OrderType.BUY, buyOrder.getPrice());
            portfolioClient.updatePorfolio(portfolioRequestDto);

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

