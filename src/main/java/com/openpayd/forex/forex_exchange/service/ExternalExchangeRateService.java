package com.openpayd.forex.forex_exchange.service;

import com.openpayd.forex.forex_exchange.model.ExchangeRate;

public interface ExternalExchangeRateService {
    ExchangeRate fetchExchangeRate(String sourceCurrency, String targetCurrency);
} 