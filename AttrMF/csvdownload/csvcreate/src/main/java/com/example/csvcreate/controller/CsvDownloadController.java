package com.example.csvcreate.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.csvcreate.model.MstKeiroEntity;
import com.example.csvcreate.service.CsvDownloadService;

import java.time.YearMonth;
import java.util.*;

import org.springframework.ui.Model;
import org.springframework.beans.factory.annotation.Autowired;

@Controller
public class CsvDownloadController {
  
    @Autowired
    private CsvDownloadService csvDownloadService;

    /**
     * CSV編集画面の表示
     * 年月選択にリダイレクト
     * 
     */
    @GetMapping("/")
    public String home() {
        return "redirect:/currentMonth";
    }

    /**
     * csvDownloadがリダイレクトされた時の処理
     * 
     */
    @GetMapping("/csvDownload")
    public String downloadPage(Model model) {

        // ワークテーブルの全データ取得
        List<MstKeiroEntity> mstList = csvDownloadService.getMstList();
        model.addAttribute("wrkList", mstList);

        // ワークテーブルの支払先・内容データを取得
        List<String> selectedPayees = csvDownloadService.getSelectedPayees();
        model.addAttribute("selectedPayees", selectedPayees);

        return "csvDownload";
    }

    /**
     * 年月が変更された際の処理
     * 
     */
    @GetMapping("/currentMonth")
    public String showCurrentMonthPage(@RequestParam(value = "selectedMonth", required = false) String selectedMonth,
                                       Model model) {

        String currentMonth;

        // 年月がnullか空欄の判定
        if (selectedMonth != null && !selectedMonth.isEmpty()) {
            // ユーザー選択の年月を取得
            currentMonth = selectedMonth;
        } else {
            // 現在の年月を取得
            currentMonth = YearMonth.now().toString();
        }

        // 取得した年月をモデルに追加
        model.addAttribute("currentMonth", currentMonth);

        Map<String, String> monthRange = csvDownloadService.getMonthRange();
        model.addAttribute("minMonth", monthRange.get("minMonth"));
        model.addAttribute("maxMonth", monthRange.get("maxMonth"));

        // 支払先一覧を取得
        model.addAttribute("selectedPayees", csvDownloadService.getSelectedPayees());

        // メッセージをモデルに追加
        model.addAttribute("message", "年月と経路を選択してください。");
        return "csvDownload";
    }

    /**
     * 経路選択された際の処理
     * 
     */
    @PostMapping("/selectPayee")
    public String filterByPayeeAndMonth(@RequestParam("selectedPayee") String selectedPayee,
                                    @RequestParam("selectedMonth") String selectedMonth,
                                    Model model) {

        // 支払先・内容がnullか空欄の判定                                    
        if (selectedPayee == null || selectedPayee.isEmpty()) {
            // メッセージをモデルに追加
            model.addAttribute("message", "経路を選択してください。");
            return "redirect:/currentMonth";
        }

        Map<String, Object> filteredData = csvDownloadService.getPayeeAndMonth(selectedPayee, selectedMonth);

        model.addAttribute("wrkList", filteredData.get("wrkList"));
        model.addAttribute("dateInfoList", filteredData.get("dateInfoList"));
        model.addAttribute("selectedPayees", csvDownloadService.getSelectedPayees());
    
        Map<String, String> monthRange = csvDownloadService.getMonthRange();
        model.addAttribute("minMonth", monthRange.get("minMonth"));
        model.addAttribute("maxMonth", monthRange.get("maxMonth"));
    
        model.addAttribute("currentMonth", selectedMonth);
        model.addAttribute("selectedPayee", selectedPayee);
        model.addAttribute("message", "指定された経路のデータを表示しました。");
    
        return "csvDownload";
    }
}
