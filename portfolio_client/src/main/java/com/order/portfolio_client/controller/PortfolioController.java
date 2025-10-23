package com.order.portfolio_client.controller;


import com.order.portfolio_client.dto.CreatePortfolioRequest;
import com.order.portfolio_client.dto.OperationPortfolioRequest;
import com.order.portfolio_client.dto.ReserveBalanceRequest;
import com.order.portfolio_client.exceptions.PortfolioAlreadyExistsException;
import com.order.portfolio_client.exceptions.PortfolioCreatingError;
import com.order.portfolio_client.exceptions.PortfolioNotFoundException;
import com.order.portfolio_client.model.Portfolio;
import com.order.portfolio_client.service.PortfolioService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/api/portfolio")
@RequiredArgsConstructor
public class PortfolioController {

    private final PortfolioService service;

    @GetMapping("/{userId}")
    public ResponseEntity<Portfolio> getPortfolio(@PathVariable Long userId) {
        Optional<Portfolio> portfolio = service.getPortfolio(userId);
        return portfolio.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping("/createPortfolio")
    public ResponseEntity<Portfolio> createPortfolio(@RequestBody CreatePortfolioRequest request) {
        return service.createPortfolio(request).map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping("/checkBalance")
    public ResponseEntity<String> hasEnoughBalance(@RequestBody OperationPortfolioRequest request) {
        try {
            String response = service.hasEnoughBalance(request);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            throw new PortfolioCreatingError("Can´t consult portfolio for user with Id" + request.getUserId().toString());
        }
    }

    @PostMapping("/reserveBalance")
    public ResponseEntity<String> reserveBalance(@RequestBody ReserveBalanceRequest request) {
        try {
            String response = service.reserveBalance(request);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            throw new PortfolioCreatingError("Can´t create portfolio for user with Id" + request.getUserId().toString());
        }


    }
}
