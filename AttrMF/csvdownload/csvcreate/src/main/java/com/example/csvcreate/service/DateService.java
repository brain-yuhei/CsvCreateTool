package com.example.csvcreate.service;

import org.springframework.stereotype.Service;

import java.time.YearMonth;
import java.util.*;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;

@Service
public class DateService {

    /**
     * 現在の年月を返す
     * 
     * @return 現在の年月（yyyy-MM形式）
     */
    public String getNowYearFormat() {
        return YearMonth.now().format(DateTimeFormatter.ofPattern("yyyy-MM"));
    } 
    
    /**
     * 2ヶ月後の範囲を返す
     * 
     * @return 年月の範囲（minMonth, maxMonth）
     */
    public Map<String, String> getMonthRange() {
        // 当年月のインスタンス生成
        YearMonth nowMonth = YearMonth.now();
        // 1カ月前の年月のインスタンス生成
        YearMonth oneMonthAgo = YearMonth.now().minusMonths(1);
        // 2カ月後年月のインスタンス生成
        YearMonth twoMonthsLater = nowMonth.plusMonths(2);
        // 年月のフォーマットを設定
        DateTimeFormatter ymFormatter = DateTimeFormatter.ofPattern("yyyy-MM");

        // 空箱を生成
        Map<String, String> result = new HashMap<>();
        result.put("minMonth", oneMonthAgo.format(ymFormatter));
        result.put("nowMonth", nowMonth.format(ymFormatter));
        result.put("maxMonth", twoMonthsLater.format(ymFormatter));
        return result;
    }

}
