package com.example.csvcreate.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;
import java.time.LocalDate;
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


    @Transactional
    public void overwriteWrkKeiroData(List<WrkKeiroEntity> newList) {
        if (newList == null || newList.isEmpty()) return;
    
        for (WrkKeiroEntity newEntry : newList) {
            LocalDate date = newEntry.getDate();
            String payee = newEntry.getPayee();
            System.out.println("更新対象日付: date = " + date);
            System.out.println("更新対象日付: payee = " + payee);
    
            // ワークテーブルから該当日付のデータを検索（日付のみ）
            WrkKeiroEntity existingEntity = wrkKeiroRepository.findByDate(date);
    
            if (existingEntity != null) {
                // 更新処理（すべての項目を上書き）
                existingEntity.setExpenseCategory(newEntry.getExpenseCategory());
                existingEntity.setPayee(newEntry.getPayee());
                existingEntity.setAmount(newEntry.getAmount());
                existingEntity.setMemo(newEntry.getMemo());
                existingEntity.setDepartmentName(newEntry.getDepartmentName());
                existingEntity.setDepartmentCode(newEntry.getDepartmentCode());
    
                wrkKeiroRepository.save(existingEntity);
                System.out.println("✅ データが上書きされました: " + date);
            } else {
                System.out.println("❌ 該当データが存在しません: " + date);
            }
        }
    }
    
    
    
    
    
    
    
    
    
}

