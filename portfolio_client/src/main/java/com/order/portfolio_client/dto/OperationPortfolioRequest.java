package com.order.portfolio_client.dto;

import com.order.portfolio_client.model.OrderType;
import lombok.Data;

@Data
public class OperationPortfolioRequest {
    private Long userId;
    private double balance;
    private String assetType;
    private OrderType orderType;
    private double pricePerAsset;
    private double assetQty;
}
