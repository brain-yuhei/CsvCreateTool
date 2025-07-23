package com.example.csvcreate.service.wrk;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;
import java.util.Comparator;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.csvcreate.model.WrkKeiroEntity;
import com.example.csvcreate.repository.WrkKeiroRepository;

@Service
public class WrkGetService {
   
    @Autowired
    private WrkKeiroRepository wrkKeiroRepository;    
    
    /**
     * 対象月のワークテーブルデータを取得処理
     * 
     * @param startDate 月初
     * @param endDate 月末
     * @return
     */
    public boolean existsWrkDataByDateOnly(LocalDate startDate, LocalDate endDate) {

        // 月初から月末のデータを取得し返す
        return wrkKeiroRepository.existsByDateBetween(startDate, endDate);

    }   
    
    /**
     * ワークテーブルから日付データを取得処理（仮）
     * 
     * @return
     */
    public List<String> getHistoryYearMonth(){

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM");

        return wrkKeiroRepository.findAll().stream()
        .map(WrkKeiroEntity::getDate)
        .map(date -> date.format(formatter))
        .distinct()
        .sorted(Comparator.reverseOrder())
        .collect(Collectors.toList());
        
    }

}
