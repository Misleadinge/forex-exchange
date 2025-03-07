package com.openpayd.forex.forex_exchange.service;

import com.openpayd.forex.forex_exchange.model.ConversionTransaction;
import com.openpayd.forex.forex_exchange.model.ExchangeRate;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDateTime;

public interface ForexService {
    ExchangeRate getExchangeRate(String sourceCurrency, String targetCurrency);
    ConversionTransaction convertCurrency(String sourceCurrency, String targetCurrency, Double amount);
    Page<ConversionTransaction> getConversionHistory(String transactionId, LocalDateTime date, Pageable pageable);
} 