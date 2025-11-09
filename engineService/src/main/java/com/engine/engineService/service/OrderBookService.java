package com.engine.engineService.service;

import com.engine.engineService.domain.Order;
import com.engine.engineService.domain.OrderType;
import com.engine.engineService.domain.Trade;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@RequiredArgsConstructor
@Slf4j
public class OrderBookService {

    private final OrderBook orderBook;
    private final PortfolioClient portfolioClient;
    private final OrderProducer orderProducer;

    public Optional<Trade> match(Order order) {
        Optional<Trade> trade = orderBook.match(order);

        trade.ifPresent(t -> {
            log.info("Trade confirmed: {}", t);

            try {
                portfolioClient.updatePorfolioFromTrade(t);
                orderProducer.sendOrder("trade-executed", t);
            } catch (Exception e) {
                log.error("Error updating portfolio or sending event: {}", e.getMessage());
            }
        });

        return trade;
    }
}

