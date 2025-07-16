package com.example.csvcreate.controller.view;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;

import com.example.csvcreate.model.WrkKeiroEntity;
import com.example.csvcreate.service.wrk.WrkCreateService;

@Controller
public class CsvTableController {

    @Autowired
    private WrkCreateService wrkCreateService; 

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
        WrkKeiroEntity wrk = wrkCreateService.updateRowByPayee(payee, date);

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
     * 入力チェック処理
     * 
     * @param koutsuuhiList
     * @return
     */
    public List<String> validateInputs(List<WrkKeiroEntity> koutsuuhiList) {
        List<String> errorMessages = new ArrayList<>();
    
        for (int i = 0; i < koutsuuhiList.size(); i++) {
            WrkKeiroEntity dto = koutsuuhiList.get(i);
    
            int displayIndex = dto.getDisplayIndex() != null ? dto.getDisplayIndex() : (i + 1);    
            String prefix = (Boolean.TRUE.equals(dto.getIsNewRow())) ? "追加行の" : "既存行の";
            String rowLabel = prefix + displayIndex + "行目";
    
            if (dto.getExpenseCategory() == null || dto.getExpenseCategory().trim().isEmpty()) {
                errorMessages.add(rowLabel + ": 経費科目が未入力です。");
            }
    
            if (dto.getAmount() == null) {
                errorMessages.add(rowLabel + ": 金額が未入力です。");
            } else {
                try {
                    new BigDecimal(dto.getAmount().toString());
                } catch (NumberFormatException e) {
                    errorMessages.add(rowLabel + ": 金額は数字で入力してください。");
                }
            }
    
            if (dto.getMemo() == null || dto.getMemo().trim().isEmpty()) {
                errorMessages.add(rowLabel + ": メモが未入力です。");
            } else if (dto.getMemo().length() > 50) {
                errorMessages.add(rowLabel + ": メモは50文字以内で入力してください。");
            }
        }
        return errorMessages;
    }    
    
}
