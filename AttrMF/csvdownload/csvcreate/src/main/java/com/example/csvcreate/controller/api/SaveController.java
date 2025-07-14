package com.example.csvcreate.controller.api;

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import com.example.csvcreate.model.CsvFormWrapperDto;
import com.example.csvcreate.model.MstKeiroEntity;
import com.example.csvcreate.model.MstKeiroFormDto;
import com.example.csvcreate.model.WrkKeiroEntity;
import com.example.csvcreate.service.DateService;
import com.example.csvcreate.service.MstKeiroService;
import com.example.csvcreate.service.MstkeiroPersistenceService;
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

    @Autowired
    MstkeiroPersistenceService mstkeiroPersistenceService;

    @Autowired
    private MessageSource messageSource;

    /**
     * 一時保存ボタン押下時の処理
     *
     * @param csvFormWrapperDto 入力データ
     * @param model              モデル
     * @return 遷移先ビュー
     */
    @PostMapping("/saveWorkTable")
    public String saveWorkTable(@ModelAttribute CsvFormWrapperDto csvFormWrapperDto, Model model) {

        String selectedMonth = csvFormWrapperDto.getSelectedMonth();
        String selectedPayee = csvFormWrapperDto.getSelectedPayee();

        List<WrkKeiroEntity> originalList = csvFormWrapperDto.getKoutsuuhiList();
        if (originalList == null || originalList.isEmpty()) {
            model.addAttribute("errormessage", messageSource.getMessage("saveWorkTableError",new String[]{}, Locale.getDefault()));
            model.addAttribute("wrkList", new ArrayList<>());
            model.addAttribute("dateInfoList", new ArrayList<>());
            model.addAttribute("selectedPayees", mstKeiroService.getSelectedPayees());
            model.addAttribute("currentMonth", selectedMonth);
            model.addAttribute("selectedPayee", selectedPayee);
            Map<String, String> monthRange = dateService.getMonthRange();
            model.addAttribute("minMonth", monthRange.get("minMonth"));
            model.addAttribute("maxMonth", monthRange.get("maxMonth"));
            return "csvTable";
        }

        // 削除された行（deleted == true）は除外
        List<WrkKeiroEntity> koutsuuhiList = originalList.stream()
            .filter(dto -> !"true".equals(dto.getDeleted()))
            .collect(Collectors.toList());  

        // 入力チェック処理
        List<String> errorMessages = validateInputs(koutsuuhiList);

        if (!errorMessages.isEmpty()) {
            model.addAttribute("errormessage", String.join("<br>", errorMessages));
            model.addAttribute("wrkList", koutsuuhiList); 
            model.addAttribute("dateInfoList", wrkTableDisplayService
                .getWrkDataForDisplay(selectedPayee, selectedMonth).get("dateInfoList"));
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
            model.addAttribute("saveSuccess", true);
        } catch (Exception e) {
            //model.addAttribute("errormessage", "同じ日付に対して同じ経路が登録されています");
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

    /**
     * 入力チェック処理
     * 
     * @param koutsuuhiList
     * @return
     */
    private List<String> validateInputs(List<WrkKeiroEntity> koutsuuhiList) {
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

    /**
     *更新ボタン押下時の処理
     * 
     * @return
     */
    @PostMapping("/viewMasterTable")
    public String saveMasterTable(@ModelAttribute MstKeiroFormDto formDto, Model model) {

        // 登録経路一覧表のデータをリスト追加
        List<MstKeiroEntity> newMstList = formDto.getMstKeiroList();

        // 入力された値をマスタテーブルに保存する処理
        mstkeiroPersistenceService.saveMstKeiroData(newMstList);

        model.addAttribute("mstdataList", newMstList);
        return "mstTableview";
    }
}

