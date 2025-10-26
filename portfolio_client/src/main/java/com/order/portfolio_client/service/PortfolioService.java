package com.order.portfolio_client.service;

import com.order.portfolio_client.dto.CreatePortfolioRequest;
import com.order.portfolio_client.dto.OperationPortfolioRequest;
import com.order.portfolio_client.dto.ReserveAssetRequest;
import com.order.portfolio_client.dto.ReserveBalanceRequest;
import com.order.portfolio_client.exceptions.PortfolioAlreadyExistsException;
import com.order.portfolio_client.exceptions.PortfolioNotFoundException;
import com.order.portfolio_client.exceptions.ReserveBalanceException;
import com.order.portfolio_client.model.OrderType;
import com.order.portfolio_client.model.Portfolio;
import com.order.portfolio_client.repository.PortfolioRepository;
import com.order.portfolio_client.repository.PortfolioRepositoryCustom;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class PortfolioService {

    private final PortfolioRepository repository;

    @Autowired
    private final PortfolioRepositoryCustom repositoryCustom;

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
            if (assetQuantity == null ){
                throw new IllegalArgumentException("Porfolio of customerId: "+ operationPortfolioRequest.getUserId() + "doesn´t have the asset " + operationPortfolioRequest.getAssetType());
            }
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

    public String reserveBalance (ReserveBalanceRequest reserveBalanceRequest){
        try {
            boolean successful = repositoryCustom.reserveBalance(reserveBalanceRequest.getUserId(), reserveBalanceRequest.getBalance());
            if (successful){
                return "OK";
            }
            else {
              return "KO";
            }
        }catch (Exception e){
            throw new ReserveBalanceException("Balance reserve couldn´t be made");
        }
    }

    public String reserveAsset (ReserveAssetRequest reserveAssetRequest){
        try {
            boolean successful = repositoryCustom.reserveAsset(reserveAssetRequest.getUserId(), reserveAssetRequest.getQuantity(), reserveAssetRequest.getAsset());
            if (successful){
                return "OK";
            }
            else {
                return "KO";
            }
        }catch (Exception e){
            throw new ReserveBalanceException("Asset reserve couldn´t be made");
        }
    }
}
