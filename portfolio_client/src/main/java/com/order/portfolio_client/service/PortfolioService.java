package com.order.portfolio_client.service;

import com.order.portfolio_client.dto.CreatePortfolioRequest;
import com.order.portfolio_client.dto.OperationPortfolioRequest;
import com.order.portfolio_client.exceptions.PortfolioAlreadyExistsException;
import com.order.portfolio_client.exceptions.PortfolioNotFoundException;
import com.order.portfolio_client.model.OrderType;
import com.order.portfolio_client.model.Portfolio;
import com.order.portfolio_client.repository.PortfolioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class PortfolioService {

    private final PortfolioRepository repository;

    public Optional<Portfolio> getPortfolio(Long userId) {
        return repository.findByUserId(userId);
    }

    public Optional<Portfolio> createPortfolio(CreatePortfolioRequest request) {
        if (repository.existsByUserId(request.getUserId())) {
            throw new PortfolioAlreadyExistsException(request.getUserId());
        }
        return Optional.of(repository.save(Portfolio.builder()
                .userId(request.getUserId())
                .balance(request.getInitialBalance() > 0 ? request.getInitialBalance() : 0.0)
                .assets(new HashMap<>())
                .build()));
    }

    public String hasEnoughBalance (OperationPortfolioRequest operationPortfolioRequest) {
        Portfolio portfolio = repository.findByUserId(operationPortfolioRequest.getUserId())
                .orElseThrow(() -> new PortfolioNotFoundException(operationPortfolioRequest.getUserId()));
        if (operationPortfolioRequest.getOrderType().equals(OrderType.SELL)){
            Double assetQuantity = portfolio.getAssets().get(operationPortfolioRequest.getAssetType());
            if (assetQuantity < operationPortfolioRequest.getAssetQty()){
                throw new IllegalArgumentException("Insufficient asset quantity for completing the sale");
            }
            else {
                return "OK";
            }
        } else {
            if (portfolio.getBalance() < (operationPortfolioRequest.getPricePerAsset() * operationPortfolioRequest.getAssetQty()) ){
                throw new IllegalArgumentException("Insufficient balance or assets");
            }
            else {
                return "OK";
            }
        }

    }
}
