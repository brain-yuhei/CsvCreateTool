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

    /**
     * CSV編集画面を開いた際の処理（現在年月の自動反映用）
     * 年月選択へリダイレクト
     * 
     */
    @GetMapping("/")
    public String home() {
        return "redirect:/currentMonth"; 
    }

    /**
     * CSV編集画面を開いた際の処理（選択経路の表示用）
     * 支払先・内容をリスト化し取得する
     * 
     */
    @GetMapping("/csvDownload")
    public String downloadPage(Model model) {

        // ワークテーブルから全データ取得
        List<WrkKeiroEntity> wrkList = wrkkeirorepository.findAll();
        // 支払先・内容をリスト化して取得
        List<String> selectedPayees = wrkList.stream()
                .map(WrkKeiroEntity::getPayee)
                .distinct()
                .collect(Collectors.toList());

        model.addAttribute("wrkList", wrkList);
        model.addAttribute("selectedPayees", selectedPayees);

        return "csvDownload";
    }

    /**
     * 年月を選択した時の処理（カレンダー選択用とカレンダー選択時の経路選択表示用）
     * 
     */
    @GetMapping("/currentMonth")
    public String showCurrentMonthPage(@RequestParam(value = "selectedMonth", required = false) String selectedMonth,
                                       Model model) {

        String currentMonth;                                
        // nullと空欄の確認
        if (selectedMonth != null && !selectedMonth.isEmpty()) {
            // nullや空欄ではない場合。（選択年月を使用）
            currentMonth = selectedMonth;
        } else {
            // nullや空欄の場合。（現在の年月を使用）
            currentMonth = YearMonth.now().toString();
        }

        model.addAttribute("currentMonth", currentMonth);

        // ワークテーブルから全件取得
        List<WrkKeiroEntity> wrkList = wrkkeirorepository.findAll();

        // 支払先・内容をリスト化して取得
        List<String> selectedPayees = wrkList.stream()
            .map(WrkKeiroEntity::getPayee)
            .filter(p -> p != null && !p.isEmpty())
            .distinct()
            .collect(Collectors.toList());

        model.addAttribute("selectedPayees", selectedPayees);
        model.addAttribute("message", "年月と経路を選択してください。");
        return "csvDownload";
    }
    
    


    /**
     * 経路選択時の処理
     * CSV管理表に表示するワークテーブルのデータをリスト化して取得
     * 
     */
    @PostMapping("/selectPayee")
    public String filterByPayeeAndMonth(@RequestParam("selectedPayee") String selectedPayee,
                                        @RequestParam("selectedMonth") String selectedMonth,
                                        Model model) {
       // nullか空欄の場合                                     
       if (selectedPayee == null || selectedPayee.isEmpty()) {
           model.addAttribute("message", "経路を選択してください。");
            return "csvDownload";
        }
    
        // 対象の年月の範囲を取得しリスト生成
        YearMonth yearMonth = YearMonth.parse(selectedMonth);
        LocalDate startDate = yearMonth.atDay(1);
        LocalDate endDate = yearMonth.atEndOfMonth();
        List<LocalDate> datesInMonth = startDate.datesUntil(endDate.plusDays(1)).collect(Collectors.toList());
    
        // 年月範囲リストを使って曜日リスト生成
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("E", Locale.JAPANESE);
        List<String> dayOfWeekList = datesInMonth.stream()
                .map(date -> date.format(formatter))
                .collect(Collectors.toList());
    
        // 選択された経路でワークテーブルからデータを取得
        WrkKeiroEntity baseRow = wrkkeirorepository.findByPayee(selectedPayee).get(0);
    
        // 月の日付分コピー
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
    
        // プルダウンの選択肢は全Payeeを再取得
        List<String> selectedPayees = wrkkeirorepository.findAll().stream()
            .map(WrkKeiroEntity::getPayee)
            .filter(p -> p != null && !p.isEmpty())
            .distinct()
            .collect(Collectors.toList());
    
        model.addAttribute("currentMonth", selectedMonth);
        model.addAttribute("selectedPayee", selectedPayee);
        model.addAttribute("wrkList", repeatedWrkList);
        model.addAttribute("dateList", datesInMonth);
        model.addAttribute("dayOfWeekList", dayOfWeekList);
        model.addAttribute("selectedPayees", selectedPayees);
        model.addAttribute("message", "指定された経路のデータを表示しました。");
    
        return "csvDownload";
    }
    
    
}


