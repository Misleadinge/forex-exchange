package com.openpayd.forex.forex_exchange.model;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.Builder;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import java.time.LocalDateTime;

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Currency conversion transaction details")
public class ConversionTransaction {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Schema(description = "Unique transaction identifier", example = "123e4567-e89b-12d3-a456-426614174000")
    private String transactionId;

    @Schema(description = "Source currency code", example = "USD")
    private String sourceCurrency;

    @Schema(description = "Target currency code", example = "EUR")
    private String targetCurrency;

    @Schema(description = "Amount in source currency", example = "100.00")
    private Double sourceAmount;

    @Schema(description = "Converted amount in target currency", example = "85.00")
    private Double targetAmount;

    @Schema(description = "Exchange rate used for conversion", example = "0.85")
    private Double exchangeRate;

    @Schema(description = "Transaction date and time", example = "2024-03-06T10:30:00")
    private LocalDateTime transactionDate;
} 