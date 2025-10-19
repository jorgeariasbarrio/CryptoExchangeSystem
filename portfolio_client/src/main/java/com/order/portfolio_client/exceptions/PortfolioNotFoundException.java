package com.order.portfolio_client.exceptions;


public class PortfolioNotFoundException extends RuntimeException {
    public PortfolioNotFoundException(Long userId) {
        super("Portfolio not found for user with userId: " + userId);
    }
}
