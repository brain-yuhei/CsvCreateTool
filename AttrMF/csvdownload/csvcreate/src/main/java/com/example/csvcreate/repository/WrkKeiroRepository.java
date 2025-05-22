package com.example.csvcreate.repository;

import com.example.csvcreate.model.WrkKeiroEntity;

import java.time.LocalDate;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;


@Repository
public interface WrkKeiroRepository extends JpaRepository<WrkKeiroEntity, Long> {

    // 重複チェック
    WrkKeiroEntity findByDateAndPayee(LocalDate date, String payee);

    void deleteByPayeeAndDateBetween(String payee, LocalDate startDate, LocalDate endDate);

    // 支払先・内容と日付が一致するデータをすべて取得
    Optional<WrkKeiroEntity> findByPayeeAndDate(String payee, LocalDate date);

    void deleteByDateIn(Set<LocalDate> dates);

    void deleteByPayeeAndDate(String payee, LocalDate date);
    
    void deleteByDateBetween(LocalDate startDate, LocalDate endDate);

    WrkKeiroEntity findByDate(LocalDate date);

    List<WrkKeiroEntity> findByDateBetweenAndPayee(LocalDate startDate, LocalDate endDate, String payee);

    @Modifying
    @Transactional
    @Query("DELETE FROM WrkKeiroEntity w WHERE w.date BETWEEN :startDate AND :endDate")
    void deleteByMonthRange(LocalDate startDate, LocalDate endDate);   

    boolean existsByDateBetween(LocalDate startDate, LocalDate endDate);

    boolean existsByDateAndPayee(LocalDate date, String payee);

    List<WrkKeiroEntity> findByDateBetween(LocalDate startDate, LocalDate endDate);

@Query("SELECT w FROM WrkKeiroEntity w WHERE w.payee LIKE %:payee% AND w.date BETWEEN :startDate AND :endDate ORDER BY w.date")
List<WrkKeiroEntity> findByPayeeAndDateBetweenOrderByDate(@Param("payee") String payee,
                                                          @Param("startDate") LocalDate startDate,
                                                          @Param("endDate") LocalDate endDate);

}



