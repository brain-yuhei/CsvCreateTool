package com.example.csvcreate.controller.view;

import java.util.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.csvcreate.controller.wrk.WrkDisplayController;
import com.example.csvcreate.service.wrk.WrkGetService;

import jakarta.servlet.http.HttpSession;

import org.springframework.ui.Model;

@Controller
public class CsvHistoryController {

    @Autowired
    private WrkGetService wrkGetService;

    @Autowired
    private WrkDisplayController wrkDisplayController;

    /**
     * 過去内容出力画面に遷移時の処理
     * 
     * @param model
     * @return
     */
    @GetMapping("/csvHistory")
    public String showCsvHistory(@RequestParam(value = "selectedMonth", required = false) String selectedMonth,
                                Model model,
                                HttpSession session) {

        session.setAttribute("selectedMonth", selectedMonth);                             

        // ワークテーブルから日付データを取得
        List<String> historyMonth = wrkGetService.getHistoryYearMonth();

        // 日付選択箇所にセット
        model.addAttribute("historyMonth", historyMonth);

        if (selectedMonth == null && !historyMonth.isEmpty()) {
            selectedMonth = historyMonth.get(0); 
        }

        if (selectedMonth != null) {

            // 選択された日付でワークデータを取得
            wrkDisplayController.showHistoryData(selectedMonth, model);
            model.addAttribute("selectedMonth", selectedMonth); 
        }

        return "csvHistory"; 
    }

}
