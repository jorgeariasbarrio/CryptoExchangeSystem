package com.engine.engineService.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import com.engine.engineService.domain.OrderType;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PortfolioRequestDto {
    private Long buyerId;
    private Long sellerId;
    private String asset;
    private double price;
    private double quantity;
}


