package com.example.csvcreate.model;

import java.math.BigDecimal;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;

import lombok.Getter;
import lombok.Setter;

/**
 * CSV管理表の1行分を管理
 * 
 */
@Entity
@Getter
@Setter
public class KoutsuuhiFormItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id; // 主キーとして追加

    private String payeeContent;
    private String expense_category;
    private BigDecimal amountInclusiveTax;
    private String memo;
    private String department_name;
    private String department_code;
    private String date;
}

