package com.order.portfolio_client.dto;

import lombok.Data;

@Data
public class CreatePortfolioRequest {
    private Long userId;
    private double initialBalance;
}
