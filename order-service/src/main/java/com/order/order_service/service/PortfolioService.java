package com.order.order_service.service;

import com.order.order_service.client.PortfolioClient;
import com.order.order_service.model.PortfolioRequestDto;
import com.order.order_service.model.ReserveAssetRequest;
import com.order.order_service.model.ReserveBalanceRequest;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.retry.annotation.Retry;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class PortfolioService {
    private final PortfolioClient portfolioClient;

    @CircuitBreaker(name = "hasEnoughBalance", fallbackMethod = "fallbackEnoughBalance")
    @Retry(name = "hasEnoughBalance")
    public String hasEnoughBalance(PortfolioRequestDto dto) {
        return portfolioClient.hasEnoughBalance(dto);
    }


    public String reserveBalance(ReserveBalanceRequest reserveBalanceRequest){
        return portfolioClient.reserveBalance(reserveBalanceRequest);
    }

    public String reserveAsset(ReserveAssetRequest reserveAssetRequest){
        return portfolioClient.reserveAsset(reserveAssetRequest);
    }
    public String updatePortfolio (PortfolioRequestDto portfolioRequestDto){
        return portfolioClient.updatePorfolio(portfolioRequestDto);
    }

    public String fallbackEnoughBalance(PortfolioRequestDto dto, Throwable t) {
        log.warn("Fallo llamando a PortfolioService.hasEnoughBalance: {}", t.getMessage());
        return "TEMPORARY_ERROR";
    }

}
