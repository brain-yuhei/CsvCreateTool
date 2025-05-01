package com.example.csvcreate.repository;

import com.example.csvcreate.model.WrkKeiroEntity;

import java.time.LocalDate;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.*;

@Repository
public interface WrkKeiroRepository extends JpaRepository<WrkKeiroEntity, Long> {

    WrkKeiroEntity findByDateAndPayee(LocalDate date, String payee);

    // 対象年月のデータを取得（Controllerで表示用）
@Query("SELECT w FROM WrkKeiroEntity w WHERE w.payee = :payee AND FUNCTION('DATE_FORMAT', w.date, '%Y-%m') = :yearMonth")
List<WrkKeiroEntity> findByPayeeAndMonth(@Param("payee") String payee, @Param("yearMonth") String yearMonth);


}

