package com.example.keirocreate.repository;

import com.example.keirocreate.model.WrkKeiroEntity;

import java.math.BigDecimal;

import org.springframework.data.jpa.repository.JpaRepository;

public interface WrkKeiroRepository extends JpaRepository<WrkKeiroEntity, Long> {
    boolean existsByPayeeAndAmount(String payee, BigDecimal amount);
}
