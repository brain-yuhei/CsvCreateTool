package com.example.csvcreate.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.csvcreate.model.WrkKeiroEntity;
import com.example.csvcreate.repository.WrkKeiroRepository;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

import org.springframework.ui.Model;
import org.springframework.beans.factory.annotation.Autowired;

@Controller
public class CsvdownloadController {

    @Autowired
    private WrkKeiroRepository wrkkeirorepository;

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
     * 全データリストと支払先・内容リストをモデルに格納
     * 
     */
    @GetMapping("/csvDownload")
    public String downloadPage(Model model) {

        // ワークテーブルから全データのリストを生成
        List<WrkKeiroEntity> wrkList = wrkkeirorepository.findAll();

        // ワークテーブルの全データリストから支払先・内容のリストを生成
        List<String> selectedPayees = wrkList.stream()
                .map(WrkKeiroEntity::getPayee)
                .distinct()
                .collect(Collectors.toList());

        // ワークテーブルの全データリストをモデルに追加       
        model.addAttribute("wrkList", wrkList);
        // 支払先・内容のリストをモデルに追加
        model.addAttribute("selectedPayees", selectedPayees);
        return "csvDownload";
    }

    /**
     * 年月が変更された際の処理
     * 年月リストと支払先・内容リストとメッセージをモデルに格納
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

        // ワークテーブルから全データのリストを生成
        List<WrkKeiroEntity> wrkList = wrkkeirorepository.findAll();

        // ワークテーブルの全データリストから支払先・内容のリストを生成
        List<String> selectedPayees = wrkList.stream()
                .map(WrkKeiroEntity::getPayee)
                .filter(p -> p != null && !p.isEmpty())
                .distinct()
                .collect(Collectors.toList());

        // 支払先・内容をモデルに追加        
        model.addAttribute("selectedPayees", selectedPayees);

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
            return "csvDownload";
        }

        // 選択された年月を年月型に変換
        YearMonth yearMonth = YearMonth.parse(selectedMonth);
        // 月初を設定
        LocalDate startDate = yearMonth.atDay(1);
        // 月末を設定
        LocalDate endDate = yearMonth.atEndOfMonth();

        // 月初から月末のリストを生成
        List<LocalDate> datesInMonth = startDate.datesUntil(endDate.plusDays(1)).collect(Collectors.toList());

        // 曜日のリストを生成
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("E", Locale.JAPANESE);

        // 日付ごとに処理するリストを生成
        List<Map<String, Object>> dateInfoList = datesInMonth.stream().map(date -> {
            // mapに対してキーと値を追加できるインスタンス生成
            Map<String, Object> map = new HashMap<>();
            map.put("date", date);
            map.put("dayOfWeek", date.format(formatter));
            DayOfWeek dayOfWeek = date.getDayOfWeek();
            map.put("checked", !(dayOfWeek == DayOfWeek.SATURDAY || dayOfWeek == DayOfWeek.SUNDAY));
            return map;
        }).collect(Collectors.toList());

        // 選択された経路とワークテーブル内の支払先・内容が同じデータを格納
        WrkKeiroEntity baseRow = wrkkeirorepository.findByPayee(selectedPayee).get(0);

        // 日付ごとにワークテーブルをリストを生成
        List<WrkKeiroEntity> repeatedWrkList = datesInMonth.stream().map(date -> {
            WrkKeiroEntity copy = new WrkKeiroEntity();
            copy.setPayee(baseRow.getPayee());
            copy.setExpenseCategory(baseRow.getExpenseCategory());
            copy.setAmount(baseRow.getAmount());
            copy.setMemo(baseRow.getMemo());
            copy.setDepartmentName(baseRow.getDepartmentName());
            copy.setDepartmentCode(baseRow.getDepartmentCode());
            copy.setDate(date);
            return copy;
        }).collect(Collectors.toList());

        // ワークテーブルの全データリストから支払先・内容のリストを生成
        List<String> selectedPayees = wrkkeirorepository.findAll().stream()
                .map(WrkKeiroEntity::getPayee)
                .filter(p -> p != null && !p.isEmpty())
                .distinct()
                .collect(Collectors.toList());

        // 選択年月をモデルに追加
        model.addAttribute("currentMonth", selectedMonth);
        // ユーザーが選択した経路をモデルに追加
        model.addAttribute("selectedPayee", selectedPayee);
        // 日付ごとのワークテーブルデータリストをモデルに追加
        model.addAttribute("wrkList", repeatedWrkList);
        // 日付ごとに処理したデータリストをモデルに追加
        model.addAttribute("dateInfoList", dateInfoList);
        // ワークテーブルの支払先・内容リストをモデルに追加
        model.addAttribute("selectedPayees", selectedPayees);
        // メッセージをモデルに追加
        model.addAttribute("message", "指定された経路のデータを表示しました。");

        return "csvDownload";
    }
}



