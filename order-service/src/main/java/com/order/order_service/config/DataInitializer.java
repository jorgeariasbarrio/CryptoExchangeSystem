package com.order.order_service.config;

import com.order.order_service.model.Order;
import com.order.order_service.model.OrderStatus;
import com.order.order_service.model.OrderType;
import com.order.order_service.repository.OrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;

@Component
public class DataInitializer implements CommandLineRunner {

    private final OrderRepository orderRepository;

    @Autowired
    public DataInitializer(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    @Override
    public void run(String... args) throws Exception {
        if (orderRepository.count() == 0) {
            Order o1 = Order.builder()
                    .userId(1L)
                    .productId("BTC-USD")
                    .type(OrderType.SELL)
                    .quantity(0.1)
                    .pricePerAsset(30000.0)
                    .orderStatus(OrderStatus.PENDING)
                    .createdAt(LocalDateTime.now())
                    .build();

            Order o2 = Order.builder()
                    .userId(2L)
                    .productId("BTC-USD")
                    .type(OrderType.BUY)
                    .quantity(0.1)
                    .pricePerAsset(30000.0)
                    .orderStatus(OrderStatus.PENDING)
                    .createdAt(LocalDateTime.now())
                    .build();

            orderRepository.saveAll(List.of(o1, o2));
            System.out.println("[data-init] Inserted sample orders");
        }
    }
}
