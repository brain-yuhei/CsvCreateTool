package com.example.csvcreate.controller.wrk;

import java.util.*;
import java.util.ArrayList;
import java.util.Locale;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import com.example.csvcreate.controller.view.CsvTableController;
import com.example.csvcreate.model.CsvFormWrapperDto;
import com.example.csvcreate.model.WrkKeiroEntity;
import com.example.csvcreate.service.DateService;
import com.example.csvcreate.service.mst.GetService;
import com.example.csvcreate.service.wrk.WrkDisplayService;
import com.example.csvcreate.service.wrk.WrkDeleteService;
import com.example.csvcreate.service.wrk.WrkSaveService;

@Controller
public class WrkSaveController {

    @Autowired
    private GetService getService;

    @Autowired
    private DateService dateService;

    @Autowired
    private WrkDeleteService wrkDeleteService;

    @Autowired
    private WrkSaveService wrkSaveService;

    @Autowired
    private WrkDisplayService wrkDisplayService;

    @Autowired
    private MessageSource messageSource;

    @Autowired
    private CsvTableController csvTableController;

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
            model.addAttribute("selectedPayees", getService.getSelectedPayees());
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
        List<String> errorMessages = csvTableController.validateInputs(koutsuuhiList);

        if (!errorMessages.isEmpty()) {
            model.addAttribute("errormessage", String.join("<br>", errorMessages));
            model.addAttribute("wrkList", koutsuuhiList); 
            model.addAttribute("dateInfoList", wrkDisplayService
                .getWrkDataForDisplay(selectedPayee, selectedMonth).get("dateInfoList"));
            model.addAttribute("selectedPayees", getService.getSelectedPayees());
            model.addAttribute("currentMonth", selectedMonth);
            model.addAttribute("selectedPayee", selectedPayee);
            Map<String, String> monthRange = dateService.getMonthRange();
            model.addAttribute("minMonth", monthRange.get("minMonth"));
            model.addAttribute("maxMonth", monthRange.get("maxMonth"));
            return "csvTable";
        }

        try {
            wrkDeleteService.deleteWrkData(selectedMonth);
            wrkSaveService.saveWrkKeiroData(koutsuuhiList); 
            model.addAttribute("saveSuccess", true);
        } catch (Exception e) {
            //model.addAttribute("errormessage", "同じ日付に対して同じ経路が登録されています");
        }

        Map<String, Object> koutsuuhiData = wrkDisplayService.getWrkDataForDisplay(selectedPayee, selectedMonth);
        model.addAttribute("wrkList", koutsuuhiData.get("wrkList"));
        model.addAttribute("dateInfoList", koutsuuhiData.get("dateInfoList"));
        model.addAttribute("selectedPayees", getService.getSelectedPayees());
        model.addAttribute("currentMonth", selectedMonth);
        model.addAttribute("selectedPayee", selectedPayee);
        Map<String, String> monthRange = dateService.getMonthRange();
        model.addAttribute("minMonth", monthRange.get("minMonth"));
        model.addAttribute("maxMonth", monthRange.get("maxMonth"));

        return "csvTable";
    }
}
