package com.example.csvcreate.model;

import java.time.LocalDate;
import lombok.Getter;
import lombok.Setter;

@Getter // Lombokを使用して、すべてのフィールドのゲッターを自動生成
@Setter // Lombokを使用して、すべてのフィールドのセッターを自動生成
public class PayeeRequestDto {
    private String payee;
    private LocalDate date;
}
