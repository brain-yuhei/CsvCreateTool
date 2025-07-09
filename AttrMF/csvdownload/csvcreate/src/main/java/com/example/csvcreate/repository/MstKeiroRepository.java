package com.example.csvcreate.repository;

import java.math.BigDecimal;

import org.springframework.data.jpa.repository.JpaRepository;
import com.example.csvcreate.model.MstKeiroEntity;

public interface MstKeiroRepository extends JpaRepository<MstKeiroEntity, Long> {

    //重複のチェック（支払先・内容と金額（税込））
    MstKeiroEntity findByPayeeContent(String payeeContent);
    // 支払先・金額のデータがあるか確認
    boolean existsByPayeeContentAndAmountInclusiveTax(String payeeContent, BigDecimal amountInclusiveTax);

}