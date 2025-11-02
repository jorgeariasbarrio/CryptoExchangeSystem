package com.order.order_service.service;

import com.order.order_service.client.PortfolioClient;
import com.order.order_service.model.*;
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
        log.info(portfolioRequestDto.toString());
        String valid = portfolioClient.hasEnoughBalance(portfolioRequestDto);


        if (!valid.equals("OK")) {
            throw new IllegalArgumentException("Insufficient balance or assets");
        }
        if (order.getType().equals(OrderType.BUY)) {
            ReserveBalanceRequest reserveBalanceRequest = new ReserveBalanceRequest(order.getUserId(), order.getQuantity() * order.getPricePerAsset());
            String validBalanceReserve = portfolioClient.reserveBalance(reserveBalanceRequest);
            if (validBalanceReserve.equals("KO")) {
                throw new IllegalArgumentException("Coudn´t reserve balance with quantity " +
                        order.getQuantity() * order.getPricePerAsset() + "for user with userId " + order.getUserId());
            }
        } else {
            ReserveAssetRequest reserveAssetRequest = new ReserveAssetRequest(order.getUserId(), order.getQuantity(), order.getProductId());
            String validBalanceReserve = portfolioClient.reserveAsset(reserveAssetRequest);
            if (validBalanceReserve.equals("KO")) {
                throw new IllegalArgumentException("Coudn´t reserve asset type " + order.getProductId() + " with quantity " +
                        order.getQuantity() + "for user with userId " + order.getUserId());
            }
        }
        order.setCreatedAt(LocalDateTime.now());

        log.info("Creating order in PENDING status...");
        order.setOrderStatus(OrderStatus.PENDING);
        Order saved = orderRepository.save(order);
        log.info("Adding request to topic");
        kafkaTemplate.send("order.pending", saved);
        return saved;
    }

    public Order completedOrder(Order order) {
        log.info("Updating order from PENDING status to COMPLETED status");
        PortfolioRequestDto portfolioRequestDto = new PortfolioRequestDto(order.getUserId(), order.getProductId(), order.getQuantity(),
                order.getType(), order.getPricePerAsset());
        portfolioRequestDto.setOrderType(order.getType());
        portfolioClient.updatePorfolio(portfolioRequestDto);
        order.setOrderStatus(OrderStatus.COMPLETED);
        orderRepository.save(order);
        log.info("Updated order with orderId {} from PENDING status to COMPLETED status", order.getId());
        return order;
    }
}


