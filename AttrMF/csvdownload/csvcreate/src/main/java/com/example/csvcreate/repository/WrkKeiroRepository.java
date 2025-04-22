package com.example.csvcreate.repository;

import java.math.BigDecimal;
import com.example.csvcreate.model.WrkKeiroEntity;

import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface WrkKeiroRepository extends JpaRepository<WrkKeiroEntity, Long> {

    // 支払先・金額のデータがあるか確認
    boolean existsByPayeeAndAmount(String payee, BigDecimal amount);

    // Payee（経路）に一致するデータをすべて取得
    List<WrkKeiroEntity> findByPayee(String payee);    

}

