package com.order.order_service.model;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "orders")
public class Order {

    @Id
    private String id;
    @NonNull
    private Long userId;
    @NonNull
    private String productId;
    @NonNull
    private OrderType type;
    @NonNull
    private double quantity;
    @NonNull
    private double pricePerAsset;

    private OrderStatus orderStatus;

    private LocalDateTime createdAt;





}



