package com.example.csvcreate.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.YearMonth;
import java.util.*;

import com.example.csvcreate.model.WrkKeiroEntity;
import com.example.csvcreate.repository.WrkKeiroRepository;

@Service
public class WrkKeiroPersistenceService {

    @Autowired
    private WrkKeiroRepository wrkKeiroRepository;    

    /**
     * CSV管理表のデータを保存処理
     *
     * @param newList 保存対象の交通費リスト
     */
    @Transactional
    public void saveWrkKeiroData(List<WrkKeiroEntity> newList) {
    
        if (newList == null || newList.isEmpty()) return;
    
        for (WrkKeiroEntity newEntry : newList) {
    
            WrkKeiroEntity entityToSave = new WrkKeiroEntity();
    
            entityToSave.setDate(newEntry.getDate());
            entityToSave.setPayee(newEntry.getPayee());
            entityToSave.setExpenseCategory(newEntry.getExpenseCategory());
            entityToSave.setAmount(newEntry.getAmount());
            entityToSave.setMemo(newEntry.getMemo());
            entityToSave.setDepartmentName(newEntry.getDepartmentName());
            entityToSave.setDepartmentCode(newEntry.getDepartmentCode());
            entityToSave.setChecked(newEntry.getChecked());
    
            wrkKeiroRepository.save(entityToSave);
        }
    }
    
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
    
}
