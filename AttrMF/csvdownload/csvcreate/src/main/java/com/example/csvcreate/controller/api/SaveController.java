package com.example.csvcreate.controller.api;

import java.math.BigDecimal;
import java.util.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import com.example.csvcreate.model.CsvFormWrapperDto;
import com.example.csvcreate.model.WrkKeiroEntity;
import com.example.csvcreate.service.DateService;
import com.example.csvcreate.service.MstKeiroService;
import com.example.csvcreate.service.WrkKeiroPersistenceService;
import com.example.csvcreate.service.WrkTableDisplayService;

@Controller
public class SaveController {

    @Autowired
    private MstKeiroService mstKeiroService; 
    
    @Autowired
    private DateService dateService;  
    
    @Autowired
    private WrkTableDisplayService wrkTableDisplayService;   
    
    @Autowired
    private WrkKeiroPersistenceService wrkKeiroPersistenceService;     

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
            } else if (dto.getMemo().length() > 50) {
                errorMessages.add((i + 1) + "メモは50文字以内で入力してください。");
            }
        }

        if (!errorMessages.isEmpty()) {
            // エラーがある場合は保存処理をスキップして再表示
            model.addAttribute("errormessage", String.join("<br>", errorMessages));
    
            Map<String, Object> koutsuuhiData = wrkTableDisplayService.getWrkDataForDisplay(selectedPayee, selectedMonth);
            model.addAttribute("wrkList", koutsuuhiList); // 編集内容を再表示
            model.addAttribute("dateInfoList", koutsuuhiData.get("dateInfoList"));
            model.addAttribute("selectedPayees", mstKeiroService.getSelectedPayees());
            model.addAttribute("currentMonth", selectedMonth);
            model.addAttribute("selectedPayee", selectedPayee);
            Map<String, String> monthRange = dateService.getMonthRange();
            model.addAttribute("minMonth", monthRange.get("minMonth"));
            model.addAttribute("maxMonth", monthRange.get("maxMonth"));
            
            return "csvTable";
        }
    
        
        try {
            wrkKeiroPersistenceService.deleteWrkData(selectedMonth);
            wrkKeiroPersistenceService.saveWrkKeiroData(koutsuuhiList);
            model.addAttribute("saveSuccess", true); // 成功フラグ
        } catch (Exception e) {
            model.addAttribute("errormessage", "保存中にエラーが発生しました: " + e.getMessage());
        }
    
        Map<String, Object> koutsuuhiData = wrkTableDisplayService.getWrkDataForDisplay(selectedPayee, selectedMonth);
        model.addAttribute("wrkList", koutsuuhiData.get("wrkList"));
        model.addAttribute("dateInfoList", koutsuuhiData.get("dateInfoList"));
        model.addAttribute("selectedPayees", mstKeiroService.getSelectedPayees());
        model.addAttribute("currentMonth", selectedMonth);
        model.addAttribute("selectedPayee", selectedPayee);
        Map<String, String> monthRange = dateService.getMonthRange();
        model.addAttribute("minMonth", monthRange.get("minMonth"));
        model.addAttribute("maxMonth", monthRange.get("maxMonth"));
    
        return "csvTable";
    }
}
