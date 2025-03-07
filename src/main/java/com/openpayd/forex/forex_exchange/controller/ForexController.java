package com.openpayd.forex.forex_exchange.controller;

import com.openpayd.forex.forex_exchange.model.ConversionTransaction;
import com.openpayd.forex.forex_exchange.model.ExchangeRate;
import com.openpayd.forex.forex_exchange.service.ForexService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.validation.annotation.Validated;

import java.time.LocalDateTime;

@RestController
@RequestMapping("/api/v1/forex")
@RequiredArgsConstructor
@Validated
@Tag(name = "Forex Exchange", description = "Endpoints for currency exchange operations")
public class ForexController {
    private final ForexService forexService;

    @Operation(
        summary = "Get Exchange Rate",
        description = "Retrieves the current exchange rate between two currencies",
        responses = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved exchange rate"),
            @ApiResponse(responseCode = "400", description = "Invalid currency code format"),
            @ApiResponse(responseCode = "500", description = "Error fetching exchange rate")
        }
    )
    @GetMapping("/rate")
    public ResponseEntity<ExchangeRate> getExchangeRate(
            @Parameter(description = "Source currency code (3 uppercase letters)", example = "USD")
            @RequestParam @Pattern(regexp = "^[A-Z]{3}$") String sourceCurrency,
            @Parameter(description = "Target currency code (3 uppercase letters)", example = "EUR")
            @RequestParam @Pattern(regexp = "^[A-Z]{3}$") String targetCurrency) {
        return ResponseEntity.ok(forexService.getExchangeRate(sourceCurrency, targetCurrency));
    }

    @Operation(
        summary = "Convert Currency",
        description = "Converts an amount from one currency to another",
        responses = {
            @ApiResponse(responseCode = "200", description = "Successfully converted currency"),
            @ApiResponse(responseCode = "400", description = "Invalid input parameters"),
            @ApiResponse(responseCode = "500", description = "Error during conversion")
        }
    )
    @PostMapping("/convert")
    public ResponseEntity<ConversionTransaction> convertCurrency(
            @Parameter(description = "Source currency code (3 uppercase letters)", example = "USD")
            @RequestParam @Pattern(regexp = "^[A-Z]{3}$") String sourceCurrency,
            @Parameter(description = "Target currency code (3 uppercase letters)", example = "EUR")
            @RequestParam @Pattern(regexp = "^[A-Z]{3}$") String targetCurrency,
            @Parameter(description = "Amount to convert", example = "100.00")
            @RequestParam @Positive Double amount) {
        return ResponseEntity.ok(forexService.convertCurrency(sourceCurrency, targetCurrency, amount));
    }

    @Operation(
        summary = "Get Conversion History",
        description = "Retrieves conversion history filtered by transaction ID or date. Date should be in ISO format (yyyy-MM-dd'T'HH:mm:ss)"
    )
    @GetMapping("/history")
    public ResponseEntity<Page<ConversionTransaction>> getConversionHistory(
            @Parameter(description = "Transaction ID to search for")
            @RequestParam(required = false) String transactionId,
            
            @Parameter(
                description = "Date to filter transactions (Format: yyyy-MM-dd'T'HH:mm:ss)",
                example = "2024-03-06T10:30:00"
            )
            @RequestParam(required = false) 
            @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss") 
            LocalDateTime date,
            
            @Parameter(
                description = "Pagination object with optional page number, size and sort parameters",
                example = "{\"page\": 0, \"size\": 10, \"sort\": [\"transactionDate,desc\"]}"
            )
            Pageable pageable) {
        
        if (transactionId == null && date == null) {
            throw new IllegalArgumentException("Either transactionId or date must be provided");
        }
        
        return ResponseEntity.ok(forexService.getConversionHistory(transactionId, date, pageable));
    }
} 