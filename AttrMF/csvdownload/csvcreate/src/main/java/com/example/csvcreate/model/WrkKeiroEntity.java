package com.example.csvcreate.model;

import java.math.BigDecimal;
import java.time.LocalDate;

import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.Getter;
import lombok.Setter;
import jakarta.persistence.Id;

@Entity // このクラスがDBのテーブルと対応することを示す
@Getter // Lombokを使用して、すべてのフィールドのゲッターを自動生成
@Setter // Lombokを使用して、すべてのフィールドのセッターを自動生成
@Table(name = "wrk_keiro", uniqueConstraints = @UniqueConstraint(columnNames = {"payee", "date"}))
public class WrkKeiroEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id; // ワークID
    private String payee; // 支払先・内容
    private String expenseCategory; // 経費科目
    @Column(precision = 10, scale = 0)
    private BigDecimal amount; // 金額
    private String memo; // メモ
    private String departmentName; // 費用負担部名
    private String departmentCode; // 費用負担部コード
    private LocalDate date;
    @Column(columnDefinition = "BIT", nullable = true)
    private Boolean checked; 
    private String deleted;
    private String rowId;

}