package com.kwonjh0406.finall.repository;

import com.kwonjh0406.finall.model.Exchange;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ExchangeRepository extends JpaRepository<Exchange, Integer> {
    Optional<Exchange> deleteByTicker(String ticker);
    Optional<Exchange> findByTicker(String ticker);
}
