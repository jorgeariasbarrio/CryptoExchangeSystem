package com.engine.engineService.domain;

import lombok.Builder;
import lombok.Data;
import lombok.NonNull;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.Instant;

@Data
@Builder
@Document(collection = "orders")
public class Order {
    @Id
    private String id;
    @NonNull
    private String userId;
    @NonNull
    private OrderType type; // BUY or SELL
    @NonNull
    private double price;
    @NonNull
    private double quantity;

    private String asset;
    @NonNull
    private OrderStatus status; // PENDING, MATCHED, CANCELLED
    @NonNull
    private Instant timestamp;

    public boolean isFilled() {
        return quantity <= 0;
    }

    public void fill(double tradedQuantity) {
        this.quantity -= tradedQuantity;
        if (this.quantity <= 0) {
            this.status = OrderStatus.FILLED;
        } else {
            this.status = OrderStatus.PARTIALLY_FILLED;
        }
    }
}
