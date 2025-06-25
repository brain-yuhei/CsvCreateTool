package com.example.csvcreate.controller.action;

import java.time.LocalDate;
import java.time.YearMonth;
import java.util.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.csvcreate.service.DateService;
import com.example.csvcreate.service.MstKeiroService;
import com.example.csvcreate.service.WrkKeiroPersistenceService;
import com.example.csvcreate.service.WrkTableDisplayService;
import com.example.csvcreate.service.WrkTableUpdateService;

@Controller
public class SelectPayeeController {

    @Autowired
    private MstKeiroService mstKeiroService; 
    
    @Autowired
    private DateService dateService;   
    
    @Autowired
    private WrkTableUpdateService wrkTableUpdateService; 
    
    @Autowired
    private WrkTableDisplayService wrkTableDisplayService;  
    
    @Autowired
    private WrkKeiroPersistenceService wrkKeiroPersistenceService;      

    SelectPayeeController(MstKeiroService mstKeiroService) {
        this.mstKeiroService = mstKeiroService;
    }    

    /**
     * 新規or履歴ボタン押下時の処理
     * 
     * @param selectedPayee
     * @param selectedMonth
     * @param actionType
     * @param model
     * @return
     */
    @PostMapping("/selectPayee")
    public String handlePayeeAction(
        @RequestParam(value = "selectedPayee", required = false) String selectedPayee,
        @RequestParam("selectedMonth") String selectedMonth,
        @RequestParam("actionType") String actionType,
        Model model) {
    
        // 新規作成時は経路必須
        if ("create".equals(actionType) && (selectedPayee == null || selectedPayee.trim().isEmpty())) {
            model.addAttribute("errormessage", "経路を選択するか、経路を登録してください。");
            model.addAttribute("currentMonth", selectedMonth);
            return "csvDownload";
        }
    
        if ("create".equals(actionType)) {
            return createNewWrkData(selectedPayee, selectedMonth, model);
        } else if ("history".equals(actionType)) {
            return showHistoryData(selectedPayee, selectedMonth, model);
        } else {
            model.addAttribute("errormessage", "無効な操作です。");
            return "csvTable";
        }
    }
 
    /**
     * ワークテーブルデータを新規作成処理
     * 
     * @param selectedPayee
     * @param selectedMonth
     * @param model
     * @return
     */
    private String createNewWrkData(String selectedPayee, String selectedMonth, Model model) {
 
        // ワークテーブル削除
        wrkTableUpdateService.deleteWrkDataByYearMonth(selectedMonth);
        // ワークテーブル作成
        wrkTableUpdateService.createWrkDataFromMaster(selectedPayee, selectedMonth);
        model.addAttribute("message", "ワークテーブルを新規に作成しました。");

        // CSV管理表に表示
        return displayWrkData(selectedPayee, selectedMonth, model);
    }
    
    /**
     * ワークテーブルデータの履歴取得処理
     * 
     * @param selectedPayee
     * @param selectedMonth
     * @param model
     * @return
     */
    private String showHistoryData(String selectedPayee, String selectedMonth, Model model) {

        // 選択年月の月初と月末を取得
        YearMonth yearMonth = YearMonth.parse(selectedMonth);
        LocalDate startDate = yearMonth.atDay(1);
        LocalDate endDate = yearMonth.atEndOfMonth();

        // ワークテーブルを月初～月末でソート
        if (!wrkKeiroPersistenceService.existsWrkDataByDateOnly(startDate, endDate)) {
            model.addAttribute("errormessage", "履歴データが存在しません。");
            model.addAttribute("wrkList", new ArrayList<>());
            model.addAttribute("dateInfoList", new ArrayList<>());
            return "csvTable";
        }
    
        model.addAttribute("message", "履歴データを表示しました。");

        // CSV管理表に表示
        return displayWrkData(selectedPayee, selectedMonth, model);
    }  
    
    /**
     * CSV管理表に表示処理
     * 
     * @param selectedPayee
     * @param selectedMonth
     * @param model
     * @return
     */
    private String displayWrkData(String selectedPayee, String selectedMonth, Model model) {
        Map<String, Object> koutsuuhiData = wrkTableDisplayService.getWrkDataForDisplay(selectedPayee, selectedMonth);
    
        model.addAttribute("wrkList", koutsuuhiData.get("wrkList"));
        model.addAttribute("dateInfoList", koutsuuhiData.get("dateInfoList"));
        model.addAttribute("selectedPayees", mstKeiroService.getSelectedPayees());
        model.addAttribute("minMonth", dateService.getMonthRange().get("minMonth"));
        model.addAttribute("maxMonth", dateService.getMonthRange().get("maxMonth"));
        model.addAttribute("currentMonth", selectedMonth);
        model.addAttribute("selectedPayee", selectedPayee);
    
        return "csvTable";
    }    

}
