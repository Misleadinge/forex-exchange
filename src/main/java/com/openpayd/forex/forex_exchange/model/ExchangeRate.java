package com.openpayd.forex.forex_exchange.model;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.Builder;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Exchange rate information between two currencies")
public class ExchangeRate {
    @Schema(description = "Source currency code", example = "USD")
    private String sourceCurrency;
    
    @Schema(description = "Target currency code", example = "EUR")
    private String targetCurrency;
    
    @Schema(description = "Exchange rate value", example = "0.85")
    private Double rate;
    
    @Schema(description = "Timestamp of the exchange rate", example = "1646582400")
    private Long timestamp;
} 