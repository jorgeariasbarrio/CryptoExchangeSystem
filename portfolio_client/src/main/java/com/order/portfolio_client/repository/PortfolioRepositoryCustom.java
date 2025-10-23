package com.order.portfolio_client.repository;

public interface PortfolioRepositoryCustom {
    boolean reserveBalance(Long userId, Double balanceToReserve);
}
