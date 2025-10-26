package com.order.portfolio_client.dto;

import lombok.Data;
import lombok.NonNull;

@Data
public class ReserveAssetRequest {
    @NonNull
    private Long userId;
    @NonNull
    private double quantity;
    @NonNull
    private String asset;
}
