package com.example.csvcreate.repository;

import com.example.csvcreate.model.WrkKeiroEntity;

import java.time.LocalDate;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;


@Repository
public interface WrkKeiroRepository extends JpaRepository<WrkKeiroEntity, Long> {

    // 重複チェック
    WrkKeiroEntity findByDateAndPayee(LocalDate date, String payee);

    void deleteByPayeeAndDateBetween(String payee, LocalDate startDate, LocalDate endDate);

    // 支払先・内容と日付が一致するデータをすべて取得
    Optional<WrkKeiroEntity> findByPayeeAndDate(String payee, LocalDate date);

}



