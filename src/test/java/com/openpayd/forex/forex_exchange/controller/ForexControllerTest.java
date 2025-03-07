package com.openpayd.forex.forex_exchange.controller;

import com.openpayd.forex.forex_exchange.model.ConversionTransaction;
import com.openpayd.forex.forex_exchange.model.ExchangeRate;
import com.openpayd.forex.forex_exchange.service.ForexService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.test.web.servlet.MockMvc;
import jakarta.validation.ConstraintViolationException;

import java.time.LocalDateTime;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.junit.jupiter.api.Assertions.assertTrue;

@WebMvcTest(ForexController.class)
class ForexControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ForexService forexService;

    @Test
    void getExchangeRate_ShouldReturnRate() throws Exception {
        // Arrange
        ExchangeRate rate = ExchangeRate.builder()
                .sourceCurrency("USD")
                .targetCurrency("EUR")
                .rate(0.85)
                .timestamp(System.currentTimeMillis())
                .build();

        when(forexService.getExchangeRate("USD", "EUR")).thenReturn(rate);

        // Act & Assert
        mockMvc.perform(get("/api/v1/forex/rate")
                .param("sourceCurrency", "USD")
                .param("targetCurrency", "EUR"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.rate").value(0.85))
                .andExpect(jsonPath("$.sourceCurrency").value("USD"))
                .andExpect(jsonPath("$.targetCurrency").value("EUR"));
    }

    @Test
    void convertCurrency_ShouldReturnConversion() throws Exception {
        // Arrange
        ConversionTransaction transaction = ConversionTransaction.builder()
                .transactionId("123")
                .sourceCurrency("USD")
                .targetCurrency("EUR")
                .sourceAmount(100.0)
                .targetAmount(85.0)
                .exchangeRate(0.85)
                .transactionDate(LocalDateTime.now())
                .build();

        when(forexService.convertCurrency(eq("USD"), eq("EUR"), eq(100.0)))
                .thenReturn(transaction);

        // Act & Assert
        mockMvc.perform(post("/api/v1/forex/convert")
                .param("sourceCurrency", "USD")
                .param("targetCurrency", "EUR")
                .param("amount", "100.0"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.transactionId").value("123"))
                .andExpect(jsonPath("$.targetAmount").value(85.0));
    }

    @Test
    void getConversionHistory_WithTransactionId_ShouldReturnTransaction() throws Exception {
        // Arrange
        ConversionTransaction transaction = ConversionTransaction.builder()
                .transactionId("123")
                .sourceCurrency("USD")
                .targetCurrency("EUR")
                .sourceAmount(100.0)
                .targetAmount(85.0)
                .build();

        when(forexService.getConversionHistory(eq("123"), any(), any(Pageable.class)))
                .thenReturn(new PageImpl<>(List.of(transaction)));

        // Act & Assert
        mockMvc.perform(get("/api/v1/forex/history")
                .param("transactionId", "123"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].transactionId").value("123"));
    }

    @Test
    void getConversionHistory_WithDate_ShouldReturnTransactions() throws Exception {
        // Arrange
        ConversionTransaction transaction = ConversionTransaction.builder()
                .transactionId("123")
                .sourceCurrency("USD")
                .targetCurrency("EUR")
                .sourceAmount(100.0)
                .targetAmount(85.0)
                .transactionDate(LocalDateTime.now())
                .build();

        when(forexService.getConversionHistory(any(), any(), any(Pageable.class)))
                .thenReturn(new PageImpl<>(List.of(transaction)));

        // Act & Assert
        mockMvc.perform(get("/api/v1/forex/history")
                .param("date", "2024-03-06T10:30:00"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].transactionId").value("123"));
    }

    @Test
    void getExchangeRate_WithInvalidCurrency_ShouldReturnBadRequest() throws Exception {
        mockMvc.perform(get("/api/v1/forex/rate")
                .param("sourceCurrency", "USD")
                .param("targetCurrency", "invalid"))  // Invalid currency format
                .andExpect(status().isBadRequest())
                .andExpect(result -> assertTrue(result.getResolvedException() instanceof ConstraintViolationException));
    }
} 