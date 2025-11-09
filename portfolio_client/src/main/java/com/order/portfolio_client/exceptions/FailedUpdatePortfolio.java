package com.order.portfolio_client.exceptions;

public class FailedUpdatePortfolio extends RuntimeException {
    public FailedUpdatePortfolio(Long userId) {
        super("Portfolio for user with : " + userId + " can´t be updated");
    }
}
