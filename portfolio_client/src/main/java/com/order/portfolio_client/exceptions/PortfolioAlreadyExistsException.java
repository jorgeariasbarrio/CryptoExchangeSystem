package com.order.portfolio_client.exceptions;

public class PortfolioAlreadyExistsException extends RuntimeException {
    public PortfolioAlreadyExistsException(Long userId) {
        super("Portfolio already exists for user with userId: " + userId);
    }
}

