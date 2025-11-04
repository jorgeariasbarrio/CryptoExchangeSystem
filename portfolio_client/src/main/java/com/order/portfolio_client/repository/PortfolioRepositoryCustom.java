package com.order.portfolio_client.repository;

import com.order.portfolio_client.dto.OperationPortfolioRequest;

public interface PortfolioRepositoryCustom {
    boolean reserveBalance(Long userId, Double balanceToReserve);
    boolean reserveAsset(Long userId, Double quantity, String assetType);
    boolean updateReservedQty(OperationPortfolioRequest operationPortfolioRequest);
}
