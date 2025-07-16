package com.example.csvcreate.service.wrk;

import java.time.LocalDate;
import java.time.YearMonth;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.csvcreate.repository.WrkKeiroRepository;

@Service
public class WrkDeleteService {
 
    @Autowired
    private WrkKeiroRepository wrkKeiroRepository;  

    /**
     * 対象月のワークテーブルデータを削除処理
     * 
     * @param selectedMonth 選択月
     */
    public void deleteWrkData(String selectedMonth) {

        // 対象月の月初から月末までを設定
        YearMonth ym = YearMonth.parse(selectedMonth);
        LocalDate startDate = ym.atDay(1);
        LocalDate endDate = ym.atEndOfMonth();
    
        // 月初から月末のデータを削除
        wrkKeiroRepository.deleteByMonthRange(startDate, endDate);
    } 
    
    /**
     * 
     * 
     * @param selectedMonth
     */
    @Transactional
    public void deleteWrkDataByYearMonth(String selectedMonth) {
        YearMonth yearMonth = YearMonth.parse(selectedMonth);
        LocalDate startDate = yearMonth.atDay(1);
        LocalDate endDate = yearMonth.atEndOfMonth();
    
        wrkKeiroRepository.deleteByDateBetween(startDate, endDate);
    }    

}
