package com.openpayd.forex.forex_exchange.service.impl;

import com.openpayd.forex.forex_exchange.model.ExchangeRate;
import com.openpayd.forex.forex_exchange.service.ExternalExchangeRateService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;
import lombok.extern.slf4j.Slf4j;

import java.util.Map;

@Slf4j
@Service
public class ExternalExchangeRateServiceImpl implements ExternalExchangeRateService {
    
    @Value("${forex.api.key}")
    private String apiKey;
    
    @Value("${forex.api.url}")
    private String apiUrl;
    
    private final RestTemplate restTemplate;

    public ExternalExchangeRateServiceImpl(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    @Override
    @Cacheable(
        value = "exchangeRates", 
        key = "#sourceCurrency + '-' + #targetCurrency",
        unless = "#result == null",
        cacheManager = "exchangeRateCacheManager"
    )
    public ExchangeRate fetchExchangeRate(String sourceCurrency, String targetCurrency) {
        log.info("Fetching exchange rate from external API for {} to {}", sourceCurrency, targetCurrency);
        String url = UriComponentsBuilder.newInstance()
                .scheme("http")
                .host("api.currencylayer.com")
                .path("/live")
                .queryParam("access_key", apiKey)
                .queryParam("source", sourceCurrency)
                .queryParam("currencies", targetCurrency)
                .queryParam("format", 1)
                .build()
                .toUriString();

        Map<String, Object> response = restTemplate.getForObject(url, Map.class);
        
        if (response == null || !Boolean.TRUE.equals(response.get("success"))) {
            throw new RuntimeException("Failed to fetch exchange rate");
        }

        Map<String, Double> quotes = (Map<String, Double>) response.get("quotes");
        Double rate = quotes.get(sourceCurrency + targetCurrency);

        return ExchangeRate.builder()
                .sourceCurrency(sourceCurrency)
                .targetCurrency(targetCurrency)
                .rate(rate)
                .timestamp(((Number) response.get("timestamp")).longValue())
                .build();
    }
} 