package com.example.csvcreate.model;

import java.math.BigDecimal;
import lombok.Getter;
import lombok.Setter;

/**
 * CSV管理表の1行分を管理
 * 
 */
@Getter // Lombokを使用して、すべてのフィールドのゲッターを自動生成
@Setter // Lombokを使用して、すべてのフィールドのセッターを自動生成
public class KoutsuuhiFormItem {

    private String payeeContent;
    private String expense_category;
    private BigDecimal amountInclusiveTax;
    private String memo;
    private String department_name;
    private String department_code;
    private String date;  
}
