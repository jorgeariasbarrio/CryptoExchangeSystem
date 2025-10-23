package com.order.portfolio_client.dto;

import lombok.Data;
import lombok.NonNull;

@Data
public class ReserveBalanceRequest {
    private Long userId;
    private double balance;
}
