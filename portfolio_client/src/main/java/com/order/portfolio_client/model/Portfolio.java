package com.order.portfolio_client.model;

import lombok.Builder;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import java.util.Map;

@Data
@Builder
@Document(collection = "portfolios")
public class Portfolio {

    @Id
    private String id;
    private Long userId;
    private double balance;
    private Map<String, Double> assets; // Ejemplo: {"BTC": 0.5, "ETH": 1.2}
}
