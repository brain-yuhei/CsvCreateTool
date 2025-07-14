package com.example.csvcreate.model;

import java.math.BigDecimal;

import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class MstCreateFormDto {
    private String payeeContent;
    private BigDecimal amountInclusiveTax;
    private String memo;
    private String expense_category;
    private String department_code;
    private String department_name;
}
