package com.order.order_service.model;

import lombok.Data;
import lombok.NonNull;

@Data
public class ReserveBalanceRequest {
    @NonNull
    private Long userId;
    @NonNull
    private double balance;
}
