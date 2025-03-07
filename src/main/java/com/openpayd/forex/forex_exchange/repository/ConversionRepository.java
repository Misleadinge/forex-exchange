package com.openpayd.forex.forex_exchange.repository;

import com.openpayd.forex.forex_exchange.model.ConversionTransaction;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;

public interface ConversionRepository extends JpaRepository<ConversionTransaction, String> {
    Page<ConversionTransaction> findByTransactionDateBetween(
            LocalDateTime startDate, 
            LocalDateTime endDate, 
            Pageable pageable
    );
} 