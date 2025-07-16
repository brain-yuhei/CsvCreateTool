package com.example.csvcreate.service.wrk;

import java.time.LocalDate;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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

}
