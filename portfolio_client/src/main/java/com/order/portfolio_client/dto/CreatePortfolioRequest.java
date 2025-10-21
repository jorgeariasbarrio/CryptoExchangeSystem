package com.order.portfolio_client.dto;

import lombok.Data;
import lombok.NonNull;

@Data
public class CreatePortfolioRequest {
    @NonNull
    private Long userId;
    @NonNull
    private Double initialBalance;
}
