package com.example.csvcreate.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.csvcreate.model.WrkKeiroEntity;
import com.example.csvcreate.repository.WrkKeiroRepository;

import java.time.LocalDate;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

import org.springframework.ui.Model;
import org.springframework.beans.factory.annotation.Autowired;

@Controller
public class CsvdownloadController {

    @Autowired
    private WrkKeiroRepository wrkkeirorepository;

    @GetMapping("/")
    public String home() {
        return "redirect:/currentMonth"; 
    }

    @GetMapping("/csvDownload")
    public String downloadPage(Model model) {
        List<WrkKeiroEntity> wrkList = wrkkeirorepository.findAll();
        List<String> selectedPayees = wrkList.stream()
                .map(WrkKeiroEntity::getPayee)
                .distinct()
                .collect(Collectors.toList());

        model.addAttribute("wrkList", wrkList);
        model.addAttribute("selectedPayees", selectedPayees);

        return "csvDownload";
    }

    /**
     * 年月の選択時に実行
     * 
     */
    @PostMapping("/currentMonth")
    public String downloadCsv(@RequestParam("selectedMonth") String selectedMonth,
                              Model model) {
    
        // ワークテーブルから全データ取得
        List<WrkKeiroEntity> wrkList = wrkkeirorepository.findAll();
    
        // ワークテーブルのデータがない場合
        if (wrkList.isEmpty()) {
            model.addAttribute("message", "データが存在しません。");
            return "csvDownload";
        }
    
        // 選択された年月の月初と月末
        YearMonth yearMonth = YearMonth.parse(selectedMonth);
        LocalDate startDate = yearMonth.atDay(1);
        LocalDate endDate = yearMonth.atEndOfMonth();
    
        // 月初〜月末の日付リスト
        List<LocalDate> datesInMonth = startDate.datesUntil(endDate.plusDays(1)).collect(Collectors.toList());
    
        // 曜日リスト
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("E", Locale.JAPANESE);
        List<String> dayOfWeekList = datesInMonth.stream()
                .map(date -> date.format(formatter))
                .collect(Collectors.toList());
    
        // ワークテーブルの1行目をベースにコピーして月末まで埋める
        // 後々、ワークテーブルのどのデータをコピーするか選択できるようにする
        WrkKeiroEntity baseRow = wrkList.get(0);
        List<WrkKeiroEntity> repeatedWrkList = datesInMonth.stream()
            .map(date -> {
                WrkKeiroEntity copy = new WrkKeiroEntity();
                copy.setPayee(baseRow.getPayee());
                copy.setExpenseCategory(baseRow.getExpenseCategory());
                copy.setAmount(baseRow.getAmount());
                copy.setMemo(baseRow.getMemo());
                copy.setDepartmentName(baseRow.getDepartmentName());
                copy.setDepartmentCode(baseRow.getDepartmentCode());
                copy.setDate(date); 
                return copy;
            })
            .collect(Collectors.toList());        

    
        // 支払先（経路）のリスト
        List<String> selectedPayees = repeatedWrkList.stream()
            .map(WrkKeiroEntity::getPayee)
            .filter(p -> p != null && !p.isEmpty())
            .distinct()
            .collect(Collectors.toList());
    
        model.addAttribute("currentMonth", selectedMonth);
        model.addAttribute("dateList", datesInMonth);
        model.addAttribute("wrkList", repeatedWrkList);
        model.addAttribute("dayOfWeekList", dayOfWeekList);
        model.addAttribute("selectedPayees", selectedPayees);
        model.addAttribute("message", "反映が完了しました。");
    
        return "csvDownload";
    }
    
}


