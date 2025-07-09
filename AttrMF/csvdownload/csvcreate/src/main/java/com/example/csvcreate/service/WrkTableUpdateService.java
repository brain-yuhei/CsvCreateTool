package com.example.csvcreate.service;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.YearMonth;
// import java.time.format.DateTimeFormatter;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.csvcreate.model.MstKeiroEntity;
import com.example.csvcreate.model.WrkKeiroEntity;
import com.example.csvcreate.repository.MstHolidayRepository;
import com.example.csvcreate.repository.MstKeiroListRepository;
import com.example.csvcreate.repository.WrkKeiroRepository;

import java.util.*;

@Service
public class WrkTableUpdateService {

    @Autowired
    private MstKeiroListRepository mstKeiroListRepository;

    @Autowired
    private WrkKeiroRepository wrkKeiroRepository;

    @Autowired
    private MstHolidayRepository mstHolidayRepository;    

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
