package com.openpayd.forex.forex_exchange.service.impl;

import com.openpayd.forex.forex_exchange.model.ConversionTransaction;
import com.openpayd.forex.forex_exchange.model.ExchangeRate;
import com.openpayd.forex.forex_exchange.repository.ConversionTransactionRepository;
import com.openpayd.forex.forex_exchange.service.ExternalExchangeRateService;
import com.openpayd.forex.forex_exchange.service.ForexService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.PageImpl;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class ForexServiceImpl implements ForexService {
    private final ExternalExchangeRateService exchangeRateService;
    private final ConversionTransactionRepository transactionRepository;

    public ForexServiceImpl(
        ExternalExchangeRateService exchangeRateService,
        ConversionTransactionRepository transactionRepository
    ) {
        this.exchangeRateService = exchangeRateService;
        this.transactionRepository = transactionRepository;
    }

    @Override
    public ExchangeRate getExchangeRate(String sourceCurrency, String targetCurrency) {
        return exchangeRateService.fetchExchangeRate(sourceCurrency, targetCurrency);
    }

    @Override
    public ConversionTransaction convertCurrency(String sourceCurrency, String targetCurrency, Double amount) {
        ExchangeRate rate = getExchangeRate(sourceCurrency, targetCurrency);
        
        ConversionTransaction transaction = ConversionTransaction.builder()
                .sourceCurrency(sourceCurrency)
                .targetCurrency(targetCurrency)
                .sourceAmount(amount)
                .targetAmount(amount * rate.getRate())
                .exchangeRate(rate.getRate())
                .transactionDate(LocalDateTime.now())
                .build();

        return transactionRepository.save(transaction);
    }

    @Override
    public Page<ConversionTransaction> getConversionHistory(String transactionId, LocalDateTime date, Pageable pageable) {
        if (transactionId != null) {
            return transactionRepository.findById(transactionId)
                    .map(transaction -> new PageImpl<ConversionTransaction>(List.of(transaction), pageable, 1))
                    .orElse(new PageImpl<>(List.of(), pageable, 0));
        }
        
        LocalDateTime startOfDay = date.toLocalDate().atStartOfDay();
        LocalDateTime endOfDay = date.toLocalDate().atTime(23, 59, 59);
        return transactionRepository.findByTransactionDateBetween(startOfDay, endOfDay, pageable);
    }
} 