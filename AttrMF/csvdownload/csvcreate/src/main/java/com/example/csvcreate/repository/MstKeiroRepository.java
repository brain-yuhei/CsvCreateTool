package com.example.csvcreate.repository;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.example.csvcreate.model.MstKeiroEntity;

public interface MstKeiroRepository extends JpaRepository<MstKeiroEntity, Long> {

    //重複のチェック（支払先・内容と金額（税込））
    MstKeiroEntity findByPayeeContent(String payeeContent);
    // 支払先・金額のデータがあるか確認
    boolean existsByPayeeContentAndAmountInclusiveTax(String payeeContent, BigDecimal amountInclusiveTax);

    // リスト型で全データ取得
    @Query("SELECT m FROM MstKeiroEntity m")
    List<MstKeiroEntity> findByAll();
}