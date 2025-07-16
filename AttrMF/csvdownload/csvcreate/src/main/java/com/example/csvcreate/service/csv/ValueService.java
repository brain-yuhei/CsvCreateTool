package com.example.csvcreate.service.csv;

import java.math.BigDecimal;

import org.springframework.stereotype.Component;

@Component
public class ValueService {
 
    /**
     * 文字列を BigDecimal に変換するメソッド
     *
     * @param value 文字列
     * @return 変換後の BigDecimal または null
     */
    public BigDecimal parseBigDecimal(String value) {
        try {
            return (value == null || value.isBlank()) ? null : new BigDecimal(value.trim());
        } catch (NumberFormatException e) {
            return null;
        }
    }    

}
