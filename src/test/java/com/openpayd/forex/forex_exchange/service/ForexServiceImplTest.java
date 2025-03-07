package com.openpayd.forex.forex_exchange.service;

import com.openpayd.forex.forex_exchange.model.ConversionTransaction;
import com.openpayd.forex.forex_exchange.model.ExchangeRate;
import com.openpayd.forex.forex_exchange.repository.ConversionTransactionRepository;
import com.openpayd.forex.forex_exchange.service.impl.ForexServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(MockitoExtension.class)
class ForexServiceImplTest {

    @Mock
    private ExternalExchangeRateService exchangeRateService;

    @Mock
    private ConversionTransactionRepository transactionRepository;

    private ForexServiceImpl forexService;

    @BeforeEach
    void setUp() {
        forexService = new ForexServiceImpl(exchangeRateService, transactionRepository);
    }

    @Test
    void getExchangeRate_ShouldReturnRate() {
        // Arrange
        ExchangeRate expectedRate = ExchangeRate.builder()
                .sourceCurrency("USD")
                .targetCurrency("EUR")
                .rate(0.85)
                .timestamp(System.currentTimeMillis())
                .build();
        
        when(exchangeRateService.fetchExchangeRate("USD", "EUR")).thenReturn(expectedRate);

        // Act
        ExchangeRate result = forexService.getExchangeRate("USD", "EUR");

        // Assert
        assertThat(result).isNotNull();
        assertThat(result.getRate()).isEqualTo(0.85);
    }

    @Test
    void convertCurrency_ShouldReturnConversion() {
        // Arrange
        ExchangeRate rate = ExchangeRate.builder()
                .sourceCurrency("USD")
                .targetCurrency("EUR")
                .rate(0.85)
                .timestamp(System.currentTimeMillis())
                .build();

        ConversionTransaction expectedTransaction = ConversionTransaction.builder()
                .sourceCurrency("USD")
                .targetCurrency("EUR")
                .sourceAmount(100.0)
                .targetAmount(85.0)
                .exchangeRate(0.85)
                .transactionDate(LocalDateTime.now())
                .build();

        when(exchangeRateService.fetchExchangeRate("USD", "EUR")).thenReturn(rate);
        when(transactionRepository.save(any())).thenReturn(expectedTransaction);

        // Act
        ConversionTransaction result = forexService.convertCurrency("USD", "EUR", 100.0);

        // Assert
        assertThat(result).isNotNull();
        assertThat(result.getTargetAmount()).isEqualTo(85.0);
    }

    @Test
    void getConversionHistory_ByTransactionId_ShouldReturnTransaction() {
        // Arrange
        String transactionId = "123";
        ConversionTransaction transaction = ConversionTransaction.builder()
                .transactionId(transactionId)
                .sourceCurrency("USD")
                .targetCurrency("EUR")
                .sourceAmount(100.0)
                .targetAmount(85.0)
                .build();

        when(transactionRepository.findById(transactionId))
                .thenReturn(Optional.of(transaction));

        // Act
        Page<ConversionTransaction> result = forexService.getConversionHistory(
                transactionId, null, Pageable.unpaged());

        // Assert
        assertThat(result).isNotNull();
        assertThat(result.getContent()).hasSize(1);
        assertThat(result.getContent().get(0).getTransactionId()).isEqualTo(transactionId);
    }

    @Test
    void getConversionHistory_ByDate_ShouldReturnTransactions() {
        // Arrange
        LocalDateTime date = LocalDateTime.now();
        List<ConversionTransaction> transactions = List.of(
                ConversionTransaction.builder()
                        .transactionId("123")
                        .sourceCurrency("USD")
                        .targetCurrency("EUR")
                        .sourceAmount(100.0)
                        .targetAmount(85.0)
                        .transactionDate(date)
                        .build()
        );

        when(transactionRepository.findByTransactionDateBetween(any(), any(), any()))
                .thenReturn(new PageImpl<>(transactions));

        // Act
        Page<ConversionTransaction> result = forexService.getConversionHistory(
                null, date, Pageable.unpaged());

        // Assert
        assertThat(result).isNotNull();
        assertThat(result.getContent()).hasSize(1);
    }
} 