package com.engine.engineService.client;

import com.engine.engineService.domain.OrderType;
import com.engine.engineService.domain.Trade;
import com.engine.engineService.model.PortfolioRequestDto;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.client.RestTemplate;

import java.util.Date;

@Component
@RequiredArgsConstructor
public class PortfolioClient {

    private final RestTemplate restTemplate;
    @Value("${portfolio.service.base-url}")
    private String baseUrl;

    public void updatePorfolioFromTrade(Trade trade) {
        PortfolioRequestDto buyerUpdate = new PortfolioRequestDto(
                trade.getBuyOrderId(), trade.getSellOrderId(), trade.getAsset(), trade.getQuantity(),
                trade.getPrice()
        );


        restTemplate.put(baseUrl + "/updatePortfolio", buyerUpdate);
    }
}
