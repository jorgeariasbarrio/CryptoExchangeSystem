package com.order.portfolio_client.config;

import com.order.portfolio_client.model.Portfolio;
import com.order.portfolio_client.repository.PortfolioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

@Component
public class DataInitializer implements CommandLineRunner {

    private final PortfolioRepository portfolioRepository;

    @Autowired
    public DataInitializer(PortfolioRepository portfolioRepository) {
        this.portfolioRepository = portfolioRepository;
    }

    @Override
    public void run(String... args) throws Exception {
        if (portfolioRepository.count() == 0) {
            Portfolio p1 = Portfolio.builder()
                    .userId(1L)
                    .balance(10000.0)
                    .assets(Map.of("BTC", 0.5, "ETH", 1.2))
                    .build();

            Portfolio p2 = Portfolio.builder()
                    .userId(2L)
                    .balance(5000.0)
                    .assets(Map.of("BTC", 0.2, "ETH", 0.5))
                    .build();

            portfolioRepository.saveAll(List.of(p1, p2));
            System.out.println("[data-init] Inserted sample portfolios");
        }
    }
}
