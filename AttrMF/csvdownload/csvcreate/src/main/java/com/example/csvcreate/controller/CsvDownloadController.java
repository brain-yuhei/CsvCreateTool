package com.example.csvcreate.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.example.csvcreate.model.CsvFormWrapperDto;
import com.example.csvcreate.model.WrkKeiroEntity;
import com.example.csvcreate.service.CsvDownloadService;
import com.example.csvcreate.service.WrkKeiroService;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.YearMonth;
import java.util.*;

import org.springframework.ui.Model;
import org.springframework.beans.factory.annotation.Autowired;

@Controller
public class CsvDownloadController {
  
    @Autowired
    private CsvDownloadService csvDownloadService;

    @Autowired
    private WrkKeiroService wrkKeiroService;

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
     * URL実行時の処理
     * 
     * @return
     */
    @GetMapping("/selectPayee")
    public String redirectToError() {
        return "redirect:/csvError";
    }

    @GetMapping("/csvError")
    public String showInvalidPage(Model model) {
        model.addAttribute("errorMessage", "不正なアクセスです。");
        return "csvError"; 
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
        csvDownloadService.deleteWrkDataByYearMonth(selectedMonth);
        // ワークテーブル作成
        csvDownloadService.createWrkDataFromMaster(selectedPayee, selectedMonth);
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
        if (!wrkKeiroService.existsWrkDataByDateOnly(startDate, endDate)) {
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
        Map<String, Object> koutsuuhiData = csvDownloadService.getWrkDataForDisplay(selectedPayee, selectedMonth);
    
        model.addAttribute("wrkList", koutsuuhiData.get("wrkList"));
        model.addAttribute("dateInfoList", koutsuuhiData.get("dateInfoList"));
        model.addAttribute("selectedPayees", csvDownloadService.getSelectedPayees());
        model.addAttribute("minMonth", csvDownloadService.getMonthRange().get("minMonth"));
        model.addAttribute("maxMonth", csvDownloadService.getMonthRange().get("maxMonth"));
        model.addAttribute("currentMonth", selectedMonth);
        model.addAttribute("selectedPayee", selectedPayee);
    
        return "csvTable";
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

    /**
     * 一時保存ボタン押下時の処理
     * 
     * @param csvFormWrapperDto
     * @param model
     * @return
     */
    @PostMapping("/saveWorkTable")
    public String saveWorkTable(@ModelAttribute CsvFormWrapperDto csvFormWrapperDto, Model model) {
    
        String selectedMonth = csvFormWrapperDto.getSelectedMonth();
        String selectedPayee = csvFormWrapperDto.getSelectedPayee();
        List<WrkKeiroEntity > koutsuuhiList = csvFormWrapperDto.getKoutsuuhiList();
    
        List<String> errorMessages = new ArrayList<>();
        
        // 必須項目のチェック
        for (int i = 0; i < koutsuuhiList.size(); i++) {
    WrkKeiroEntity dto = koutsuuhiList.get(i);

    if (dto.getExpenseCategory() == null || dto.getExpenseCategory().trim().isEmpty()) {
        errorMessages.add((i + 1) + "行目: 経費科目が未入力です。");
    }

    if (dto.getAmount() == null) {
        errorMessages.add((i + 1) + "行目: 金額が未入力です。");
    } else {
        // ここで数字チェック（BigDecimalの文字列変換で例外が出れば非数字）
        try {
            new BigDecimal(dto.getAmount().toString());
        } catch (NumberFormatException e) {
            errorMessages.add((i + 1) + "行目: 金額は数字で入力してください。");
        }
    }

    if (dto.getMemo() == null || dto.getMemo().trim().isEmpty()) {
        errorMessages.add((i + 1) + "行目: メモが未入力です。");
    } else if (dto.getMemo().length() > 30) {
        errorMessages.add((i + 1) + "行目: メモは30文字以内で入力してください。");
    }
}

    
        if (!errorMessages.isEmpty()) {
            // エラーがある場合は保存処理をスキップして再表示
            model.addAttribute("errormessage", String.join("<br>", errorMessages));
    
            Map<String, Object> koutsuuhiData = csvDownloadService.getWrkDataForDisplay(selectedPayee, selectedMonth);
            model.addAttribute("wrkList", koutsuuhiList); // 編集内容を再表示
            model.addAttribute("dateInfoList", koutsuuhiData.get("dateInfoList"));
            model.addAttribute("selectedPayees", csvDownloadService.getSelectedPayees());
            model.addAttribute("currentMonth", selectedMonth);
            model.addAttribute("selectedPayee", selectedPayee);
            Map<String, String> monthRange = csvDownloadService.getMonthRange();
            model.addAttribute("minMonth", monthRange.get("minMonth"));
            model.addAttribute("maxMonth", monthRange.get("maxMonth"));
            
            return "csvTable";
        }
    
        
        try {
            wrkKeiroService.deleteWrkData(selectedMonth);
            wrkKeiroService.saveWrkKeiroData(koutsuuhiList);
            model.addAttribute("saveSuccess", true); // 成功フラグ
        } catch (Exception e) {
            model.addAttribute("errormessage", "保存中にエラーが発生しました: " + e.getMessage());
        }
    
        Map<String, Object> koutsuuhiData = csvDownloadService.getWrkDataForDisplay(selectedPayee, selectedMonth);
        model.addAttribute("wrkList", koutsuuhiData.get("wrkList"));
        model.addAttribute("dateInfoList", koutsuuhiData.get("dateInfoList"));
        model.addAttribute("selectedPayees", csvDownloadService.getSelectedPayees());
        model.addAttribute("currentMonth", selectedMonth);
        model.addAttribute("selectedPayee", selectedPayee);
        Map<String, String> monthRange = csvDownloadService.getMonthRange();
        model.addAttribute("minMonth", monthRange.get("minMonth"));
        model.addAttribute("maxMonth", monthRange.get("maxMonth"));
    
        return "csvTable";
    }
}
