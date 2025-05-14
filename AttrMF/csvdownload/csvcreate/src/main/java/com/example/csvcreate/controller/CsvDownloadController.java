package com.example.csvcreate.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
// import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
// import org.springframework.transaction.annotation.Transactional;

import com.example.csvcreate.model.CsvFormWrapperDto;
// import com.example.csvcreate.model.CsvFormWrapperDto;
import com.example.csvcreate.model.WrkKeiroEntity;
// import com.example.csvcreate.repository.WrkKeiroRepository;
import com.example.csvcreate.service.CsvDownloadService;
import com.example.csvcreate.service.WrkKeiroService;

import java.time.LocalDate;
import java.time.YearMonth;
import java.util.*;
// import java.util.stream.Collectors;

import org.springframework.ui.Model;
import org.springframework.beans.factory.annotation.Autowired;

@Controller
public class CsvDownloadController {
  
    @Autowired
    private CsvDownloadService csvDownloadService;

    @Autowired
    private WrkKeiroService wrkKeiroService;

    // @Autowired
    // private WrkKeiroRepository wrkKeiroRepository;

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
     * 年月選択時の処理
     * 
     * @param selectedMonth
     * @param model
     * @return
     */
    @GetMapping("/currentMonth")
    public String showCurrentMonthPage(@RequestParam(value = "selectedMonth", required = false) String selectedMonth,
                                       @RequestParam(value = "selectedPayee", required = false) String selectedPayee,
                                       Model model) {
    
        String currentMonth;
    
        // 年月がnullか空欄の判定
        if (selectedMonth != null && !selectedMonth.isEmpty()) {
            currentMonth = selectedMonth;
        } else {
            currentMonth = YearMonth.now().toString();
        }
    
        // 支払先一覧を取得しモデルに追加
        List<String> payees = csvDownloadService.getSelectedPayees();
        model.addAttribute("selectedPayees", payees);
    
        if (selectedPayee == null || selectedPayee.isEmpty()) {
            selectedPayee = payees.stream().findFirst().orElse("");
        }
    
        // ワークテーブルのデータを取得しモデルに追加
        List<WrkKeiroEntity> wrkDataList = wrkKeiroService.getWrkKeiroData(currentMonth, selectedPayee);
        model.addAttribute("koutsuuhiList", wrkDataList);
    
        // 年月・Payeeも再表示用に追加
        model.addAttribute("currentMonth", currentMonth);
        model.addAttribute("selectedPayee", selectedPayee);
    
        //年月の範囲を設定し最小月と最大月をモデルに追加
        Map<String, String> monthRange = csvDownloadService.getMonthRange();
        model.addAttribute("minMonth", monthRange.get("minMonth"));
        model.addAttribute("maxMonth", monthRange.get("maxMonth"));
    
        return "csvDownload"; // CSV管理表のJSP名
    }
    

    /**
     * CSV管理表生成ボタンを押下時の処理
     * 
     * @param selectedPayee
     * @param selectedMonth
     * @param model
     * @return
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

        // ワークテーブルの生成処理
        csvDownloadService.createWorkTableData(selectedPayee, selectedMonth);

        // 生成したワークテーブルデータを成型して取得
        Map<String, Object> koutsuuhiData = csvDownloadService.getPayeeAndMonth(selectedPayee, selectedMonth);

        // ワークテーブから各日付のデータリストとチェック有無のリストをモデルに追加
        model.addAttribute("wrkList", koutsuuhiData.get("wrkList"));
        model.addAttribute("dateInfoList", koutsuuhiData.get("dateInfoList"));

        // 支払先一覧を取得しモデルに追加
        model.addAttribute("selectedPayees", csvDownloadService.getSelectedPayees());
    
        //年月の範囲を設定し最小月と最大月をモデルに追加 
        Map<String, String> monthRange = csvDownloadService.getMonthRange();
        model.addAttribute("minMonth", monthRange.get("minMonth"));
        model.addAttribute("maxMonth", monthRange.get("maxMonth"));

        // 選択年月をモデルに追加
        model.addAttribute("currentMonth", selectedMonth);

        // 選択経路をモデルに追加
        model.addAttribute("selectedPayee", selectedPayee);
        // model.addAttribute("message", "指定された経路のデータを表示しました。");
    
        return "csvDownload";
    }

    /**
     * 支払先・内容を変更時の処理
     * 
     * @param request 画面から送られてきたデータを受け取る
     * @return
     */
    @PostMapping("/api/updateRowByPayee")
    @ResponseBody
    public Map<String, Object> updateRowByPayee(@RequestBody Map<String, String> request) {

        // 支払先・内容のデータを取得
        String payee = request.get("payee");

        // 日付のデータを取得
        LocalDate date = LocalDate.parse(request.get("date"));

        // 支払先・内容と日付を使い1行分のワークテーブルデータを作成する
        WrkKeiroEntity wrk = wrkKeiroService.updateRowByPayee(payee, date);

        // データがない場合は空データを返す
        if (wrk == null) {
            return Collections.emptyMap();
        }

        // ワークテーブルデータがある場合は、各項目を1つづつ取り出す
        Map<String, Object> response = new HashMap<>();
        response.put("payeeContent", wrk.getPayee());
        response.put("expense_category", wrk.getExpenseCategory());
        response.put("amountInclusiveTax", wrk.getAmount());
        response.put("memo", wrk.getMemo());
        response.put("department_name", wrk.getDepartmentName());
        response.put("department_code", wrk.getDepartmentCode());

        return response;
    }

    @PostMapping("/saveWorkTable")
    public String saveWorkTable(@ModelAttribute CsvFormWrapperDto csvFormWrapperDto, Model model) {
        // フォームから選択された年月・経路を取得
        String selectedMonth = csvFormWrapperDto.getSelectedMonth();
        String selectedPayee = csvFormWrapperDto.getSelectedPayee();
        try {
            // ワークテーブルの削除
            csvDownloadService.deleteWrkData(selectedMonth);
            // CSV管理表の編集データを保存
            wrkKeiroService.saveWrkKeiroData(csvFormWrapperDto.getKoutsuuhiList());
    
            model.addAttribute("message", "データを一時保存しました。");
        } catch (Exception e) {
            model.addAttribute("message", "保存中にエラーが発生しました: " + e.getMessage());
        }
    
        // 再表示用データを取得
        Map<String, Object> koutsuuhiData = csvDownloadService.getPayeeAndMonth(selectedPayee, selectedMonth);

        // 新再表示用データを取得
        // Map<String, Object> koutsuuhiData = csvDownloadService.getWrkTable(selectedPayee, selectedMonth);
  
        model.addAttribute("wrkList", koutsuuhiData.get("wrkList"));
        model.addAttribute("dateInfoList", koutsuuhiData.get("dateInfoList"));
        model.addAttribute("selectedPayees", csvDownloadService.getSelectedPayees());
        model.addAttribute("currentMonth", selectedMonth);
        model.addAttribute("selectedPayee", selectedPayee);
    
        Map<String, String> monthRange = csvDownloadService.getMonthRange();
        model.addAttribute("minMonth", monthRange.get("minMonth"));
        model.addAttribute("maxMonth", monthRange.get("maxMonth"));
    
        return "csvDownload";
    }
    


}
