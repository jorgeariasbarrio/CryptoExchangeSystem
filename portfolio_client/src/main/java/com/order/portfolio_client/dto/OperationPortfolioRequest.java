package com.order.portfolio_client.dto;

import com.mongodb.annotations.NotThreadSafe;
import com.order.portfolio_client.model.OrderType;
import lombok.Data;
import lombok.NonNull;

@Data
public class OperationPortfolioRequest {
    @NonNull
    private Long userId;
    private double balance;
    private String assetType;
    @NonNull
    private OrderType orderType;
    private double pricePerAsset;
    private double assetQty;
}
