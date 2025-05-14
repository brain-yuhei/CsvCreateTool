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
 * ワークテーブルにCSV管理表のデータを新規保存する
 *
 * @param newList 保存対象の交通費リスト
 */
@Transactional
public void saveWrkKeiroData(List<WrkKeiroEntity> newList) {
    if (newList == null || newList.isEmpty()) return;

    for (WrkKeiroEntity newEntry : newList) {
        WrkKeiroEntity entityToSave = new WrkKeiroEntity();

        // 個別に各項目を設定
        entityToSave.setDate(newEntry.getDate());
        entityToSave.setPayee(newEntry.getPayee());
        entityToSave.setExpenseCategory(newEntry.getExpenseCategory());
        entityToSave.setAmount(newEntry.getAmount());
        entityToSave.setMemo(newEntry.getMemo());
        entityToSave.setDepartmentName(newEntry.getDepartmentName());
        entityToSave.setDepartmentCode(newEntry.getDepartmentCode());

        wrkKeiroRepository.save(entityToSave);

        System.out.println("🆕 データを新規保存しました: 日付 = " + entityToSave.getDate() + ", 支払先 = " + entityToSave.getPayee());
    }
}



    /**
     * 
     * @param yearMonth
     * @param payee
     * @return
     */
    public List<WrkKeiroEntity> getWrkKeiroData(String yearMonth, String payee) {
        YearMonth ym = YearMonth.parse(yearMonth);
        LocalDate startDate = ym.atDay(1);
        LocalDate endDate = ym.atEndOfMonth();
        return wrkKeiroRepository.findByDateBetweenAndPayee(startDate, endDate, payee);
    }

  
}

