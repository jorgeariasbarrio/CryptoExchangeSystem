package com.order.order_service.model;

import lombok.Data;

@Data
public class PortfolioRequestDto {
    private Long userId;
    private double balance;
    private String assetType;
    private OrderType orderType;
    private Double pricePerAsset;
    private Double assetQty;

    public PortfolioRequestDto(long userId, String assetType, double balance, OrderType orderType, Double pricePerAsset) {
        this.userId = userId;
        this.assetType = assetType;
        this.balance = balance;
        this.orderType = orderType;
        this.pricePerAsset = pricePerAsset;
    }
}

