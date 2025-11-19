package com.engine.engineService.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;
import java.util.Date;

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


    public Trade (Long buyOrderId, Long sellOrderId, String asset, double price, double quantity){
        this.buyOrderId = buyOrderId;
        this.sellOrderId = sellOrderId;
        this.asset = asset;
        this.price = price;
        this.quantity = quantity;
        this.timestamp = new Date().getTime();
    }
}
