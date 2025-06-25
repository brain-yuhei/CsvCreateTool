package com.example.csvcreate.service;

import java.time.LocalDate;
import java.util.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.csvcreate.model.MstKeiroEntity;
import com.example.csvcreate.model.WrkKeiroEntity;
import com.example.csvcreate.repository.MstKeiroRepository;

@Service
public class WrkKeiroCreateService {
 
    @Autowired
    private MstKeiroRepository mstKeiroRepository; 

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

}
