package com.order.order_service.client;

import com.order.order_service.model.PortfolioRequestDto;
import com.order.order_service.model.ReserveAssetRequest;
import com.order.order_service.model.ReserveBalanceRequest;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
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

    public PortfolioRequestDto getPortfolio(@PathVariable("userId") Long userId){
        return restTemplate.getForObject(baseUrl + "/" + userId.toString(), PortfolioRequestDto.class);
    }

    public String hasEnoughBalance(@RequestBody PortfolioRequestDto request){
        return restTemplate.postForObject(baseUrl + "/checkBalance", request, String.class);
    }

    public String reserveBalance(@RequestBody ReserveBalanceRequest request){
        return restTemplate.postForObject(baseUrl + "/reserveBalance", request, String.class);
    }

    public String reserveAsset(@RequestBody ReserveAssetRequest request){
        return restTemplate.postForObject(baseUrl + "/reserveAsset", request, String.class);
    }
}
