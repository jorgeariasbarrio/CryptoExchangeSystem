package com.engine.engineService.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Trade {
    private Long buyOrderId;
    private Long sellOrderId;
    private String asset;
    private double price;
    private double quantity;
    private long timestamp;
}
