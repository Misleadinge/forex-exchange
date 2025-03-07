package com.openpayd.forex.forex_exchange.repository;

import com.openpayd.forex.forex_exchange.model.ConversionTransaction;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;

@Repository
public interface ConversionTransactionRepository extends JpaRepository<ConversionTransaction, String> {
    Page<ConversionTransaction> findByTransactionDate(LocalDateTime date, Pageable pageable);
    Page<ConversionTransaction> findByTransactionDateBetween(LocalDateTime startDate, LocalDateTime endDate, Pageable pageable);
} 