package com.engine.engineService.client;

import com.engine.engineService.model.PortfolioRequestDto;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.client.RestTemplate;

@Component
public class PortfolioClient {

    private final RestTemplate restTemplate;
    private final String baseUrl;

    public PortfolioClient(@Value("${portfolio.service.base-url}") String baseUrl) {
        this.restTemplate = new RestTemplate();
        this.baseUrl = baseUrl;
    }
    public String updatePorfolio(@RequestBody PortfolioRequestDto request){
        return restTemplate.exchange(
                baseUrl + "/updatePorfolio",
                HttpMethod.PUT,
                new HttpEntity<>(request),
                String.class
        ).toString();
    }
}
