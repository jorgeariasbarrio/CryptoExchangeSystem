package com.order.portfolio_client.repository;

import com.order.portfolio_client.model.Portfolio;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface PortfolioRepository extends MongoRepository<Portfolio, String> {
    Optional<Portfolio> findByUserId(Long userId);

    boolean existsByUserId(Long userId);
}