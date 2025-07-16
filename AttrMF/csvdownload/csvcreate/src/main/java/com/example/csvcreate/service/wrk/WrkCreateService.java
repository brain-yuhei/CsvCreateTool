package com.example.csvcreate.service.wrk;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.YearMonth;
import java.util.*;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.csvcreate.model.MstKeiroEntity;
import com.example.csvcreate.model.WrkKeiroEntity;
import com.example.csvcreate.repository.MstHolidayRepository;
import com.example.csvcreate.repository.MstKeiroListRepository;
import com.example.csvcreate.repository.WrkKeiroRepository;

@Service
public class WrkCreateService {
  
    @Autowired
    private MstKeiroListRepository mstKeiroListRepository; 

    @Autowired
    private WrkKeiroRepository wrkKeiroRepository;

    @Autowired
    private MstHolidayRepository mstHolidayRepository;        

    /**
     * 1行分のワークテーブルデータを作成
     * 
     * @param payee
     * @param date
     * @return
     */
    public WrkKeiroEntity updateRowByPayee(String payee, LocalDate date) {

        // 選択経路と一致する支払先・内容のデータ一覧を取得
        List<MstKeiroEntity> matchedList = mstKeiroListRepository.findByPayeeContent(payee);

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
     * ワークテーブルデータを生成処理
     * 
     * @param selectedPayee
     * @param selectedMonth
     */
    @Transactional
    public void createWrkDataFromMaster(String selectedPayee, String selectedMonth) {
    
        YearMonth yearMonth = YearMonth.parse(selectedMonth);
        LocalDate startDate = yearMonth.atDay(1);
        LocalDate endDate = yearMonth.atEndOfMonth();
    
        List<LocalDate> datesInMonth = startDate.datesUntil(endDate.plusDays(1)).collect(Collectors.toList());
    
        MstKeiroEntity baseDate = mstKeiroListRepository.findByPayeeContent(selectedPayee).get(0);
    
        for (LocalDate date : datesInMonth) {
            WrkKeiroEntity entity = new WrkKeiroEntity();
    
            entity.setPayee(baseDate.getPayeeContent());
            entity.setExpenseCategory(baseDate.getExpense_category());
            entity.setAmount(baseDate.getAmountInclusiveTax());
            entity.setMemo(baseDate.getMemo());
            entity.setDepartmentName(baseDate.getDepartment_name());
            entity.setDepartmentCode(baseDate.getDepartment_code());
            entity.setDate(date);
    
            // ✅ チェック状態の初期登録
            boolean isWeekday = !(date.getDayOfWeek() == DayOfWeek.SATURDAY || date.getDayOfWeek() == DayOfWeek.SUNDAY);
            boolean isHoliday = mstHolidayRepository.existsByHolidayDate(date);
            entity.setChecked(isWeekday && !isHoliday); // ← チェック状態をセット
    
            wrkKeiroRepository.save(entity);
        }
    }    

}
