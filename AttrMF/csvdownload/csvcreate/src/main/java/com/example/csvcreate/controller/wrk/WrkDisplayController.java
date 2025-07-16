package com.example.csvcreate.controller.wrk;

import java.time.LocalDate;
import java.time.YearMonth;
import java.util.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;

import com.example.csvcreate.service.DateService;
import com.example.csvcreate.service.mst.GetService;
import com.example.csvcreate.service.wrk.WrkGetService;
import com.example.csvcreate.service.wrk.WrkDisplayService;

@Controller
public class WrkDisplayController {

    @Autowired
    private WrkDisplayService wrkDisplayService; 

    @Autowired
    private DateService dateService; 
    
    @Autowired
    private GetService getService;
    
    @Autowired
    private WrkGetService wrkGetService;  
    
    @Autowired
    private MessageSource messageSource;    
   
    /**
     * CSV管理表に表示処理
     * 
     * @param selectedPayee
     * @param selectedMonth
     * @param model
     * @return
     */
    public String displayWrkData(String selectedPayee, String selectedMonth, Model model) {
        Map<String, Object> koutsuuhiData = wrkDisplayService.getWrkDataForDisplay(selectedPayee, selectedMonth);
    
        model.addAttribute("wrkList", koutsuuhiData.get("wrkList"));
        model.addAttribute("dateInfoList", koutsuuhiData.get("dateInfoList"));
        model.addAttribute("selectedPayees", getService.getSelectedPayees());
        model.addAttribute("minMonth", dateService.getMonthRange().get("minMonth"));
        model.addAttribute("maxMonth", dateService.getMonthRange().get("maxMonth"));
        model.addAttribute("currentMonth", selectedMonth);
        model.addAttribute("selectedPayee", selectedPayee);
    
        return "csvTable";
    }  
    
    /**
     * ワークテーブルデータの履歴取得処理
     * 
     * @param selectedPayee
     * @param selectedMonth
     * @param model
     * @return
     */
    public String showHistoryData(String selectedPayee, String selectedMonth, Model model) {

        // 選択年月の月初と月末を取得
        YearMonth yearMonth = YearMonth.parse(selectedMonth);
        LocalDate startDate = yearMonth.atDay(1);
        LocalDate endDate = yearMonth.atEndOfMonth();

        // ワークテーブルを月初～月末でソート
        if (!wrkGetService.existsWrkDataByDateOnly(startDate, endDate)) {
            model.addAttribute("errormessage", messageSource.getMessage("historyDataError",new String[]{}, Locale.getDefault()));
            model.addAttribute("wrkList", new ArrayList<>());
            model.addAttribute("dateInfoList", new ArrayList<>());
            return "csvTable";
        }
    
        model.addAttribute("message", messageSource.getMessage("historyData",new String[]{}, Locale.getDefault()));

        // CSV管理表に表示
        return displayWrkData(selectedPayee, selectedMonth, model);
    }     

}
