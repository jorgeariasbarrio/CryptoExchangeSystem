package com.order.order_service.service;

import com.order.order_service.client.PortfolioClient;
import com.order.order_service.model.Order;
import com.order.order_service.model.OrderType;
import com.order.order_service.model.PortfolioRequestDto;
import com.order.order_service.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Slf4j
@Service
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;
    private final KafkaTemplate<String, Order> kafkaTemplate;
    private final PortfolioClient portfolioClient;

    public Order createOrder(Order order) {


        PortfolioRequestDto portfolioRequestDto = new PortfolioRequestDto(order.getUserId(), order.getProductId(), order.getQuantity(), order.getType(), order.getPricePerAsset());
        String valid = portfolioClient.hasEnoughBalance(portfolioRequestDto);


        if (!valid.equals("OK")) {
            throw new IllegalArgumentException("Insufficient balance or assets");
        }

        order.setStatus("CREATED");
        order.setCreatedAt(LocalDateTime.now());

        log.info("Creating order ...");
        Order saved = orderRepository.save(order);
        log.info("Adding request to topic");
        kafkaTemplate.send("order.created", saved);

        return saved;
    }
}


