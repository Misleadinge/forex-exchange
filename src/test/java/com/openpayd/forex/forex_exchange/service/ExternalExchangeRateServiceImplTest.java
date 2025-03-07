package com.openpayd.forex.forex_exchange.service;

import com.openpayd.forex.forex_exchange.model.ExchangeRate;
import com.openpayd.forex.forex_exchange.service.impl.ExternalExchangeRateServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.web.client.RestTemplate;
import org.springframework.boot.test.web.client.TestRestTemplate;

import java.util.HashMap;
import java.util.Map;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

@ExtendWith(MockitoExtension.class)
class ExternalExchangeRateServiceImplTest {

    @Mock
    private RestTemplate restTemplate;

    private ExternalExchangeRateServiceImpl exchangeRateService;

    private TestRestTemplate testRestTemplate = new TestRestTemplate();

    @BeforeEach
    void setUp() {
        exchangeRateService = new ExternalExchangeRateServiceImpl(restTemplate);
        ReflectionTestUtils.setField(exchangeRateService, "apiKey", "test-api-key");
        ReflectionTestUtils.setField(exchangeRateService, "apiUrl", "http://api.test.com");
    }

    @Test
    void fetchExchangeRate_ShouldReturnRate() {
        // Arrange
        Map<String, Object> response = new HashMap<>();
        response.put("success", true);
        response.put("timestamp", 1614960000L);
        
        Map<String, Double> quotes = new HashMap<>();
        quotes.put("USDEUR", 0.85);
        response.put("quotes", quotes);

        when(restTemplate.getForObject(anyString(), any())).thenReturn(response);

        // Act
        ExchangeRate result = exchangeRateService.fetchExchangeRate("USD", "EUR");

        // Assert
        assertThat(result).isNotNull();
        assertThat(result.getRate()).isEqualTo(0.85);
        assertThat(result.getSourceCurrency()).isEqualTo("USD");
        assertThat(result.getTargetCurrency()).isEqualTo("EUR");
    }

    @Test
    void fetchExchangeRate_WhenApiCallFails_ShouldThrowException() {
        // Arrange
        Map<String, Object> response = new HashMap<>();
        response.put("success", false);

        when(restTemplate.getForObject(anyString(), any())).thenReturn(response);

        // Act & Assert
        assertThrows(RuntimeException.class, () -> 
            exchangeRateService.fetchExchangeRate("USD", "EUR")
        );
    }
} 