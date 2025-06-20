package com.example.csvcreate.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.time.LocalDate;
import java.time.YearMonth;

import com.example.csvcreate.model.MstKeiroEntity;
import com.example.csvcreate.model.WrkKeiroEntity;
import com.example.csvcreate.repository.MstKeiroRepository;
import com.example.csvcreate.repository.WrkKeiroRepository;

@Service
public class WrkKeiroService {

    @Autowired
    private MstKeiroRepository mstKeiroRepository;

    @Autowired
    private WrkKeiroRepository wrkKeiroRepository;

    /**
     * 1行分のワークテーブルデータを作成
     * 
     * @param payee
     * @param date
     * @return
     */
    public WrkKeiroEntity updateRowByPayee(String payee, LocalDate date) {

        // 選択経路と一致する支払先・内容のデータ一覧を取得
        List<MstKeiroEntity> matchedList = mstKeiroRepository.findByPayeeContent(payee);

        // データ一覧が空の場合はNullを返す
        if (matchedList.isEmpty()) {
            return null;
        }
    
        // データ一覧の1件目を取得
        MstKeiroEntity matched = matchedList.get(0); 
    
        WrkKeiroEntity wrk = new WrkKeiroEntity();
        wrk.setDate(date);
        wrk.setPayee(matched.getPayeeContent());
        wrk.setExpenseCategory(matched.getExpense_category());
        wrk.setAmount(matched.getAmountInclusiveTax());
        wrk.setMemo(matched.getMemo());
        wrk.setDepartmentName(matched.getDepartment_name());
        wrk.setDepartmentCode(matched.getDepartment_code());
    
        return wrk;
    }

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

