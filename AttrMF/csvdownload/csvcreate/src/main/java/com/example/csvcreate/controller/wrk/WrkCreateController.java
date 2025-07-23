package com.example.csvcreate.controller.wrk;

import java.util.Locale;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.csvcreate.service.wrk.WrkDeleteService;
import com.example.csvcreate.service.wrk.WrkCreateService;

@Controller
public class WrkCreateController {
 
    @Autowired
    private WrkDeleteService wrkDeleteService;
    
    @Autowired
    private WrkCreateService wrkCreateService;     
    
    @Autowired
    private MessageSource messageSource; 
    
    @Autowired
    private WrkDisplayController wrkDisplayController;

    WrkCreateController(WrkDisplayController wrkDisplayController) {
        this.wrkDisplayController = wrkDisplayController;
    }

    /**
     * 
     * 
     * @param selectedPayee
     * @param selectedMonth
     * @param model
     * @return
     */
    @PostMapping("/csvDeleteHistory")
    public String csvDeleteAndCreate(@RequestParam("selectedPayee") String selectedPayee,
                                     @RequestParam("selectedMonth") String selectedMonth,
                                     Model model) {
        return createNewWrkData(selectedPayee, selectedMonth, model);
    }

    /**
     * ワークテーブルデータを新規作成処理
     * 
     * @param selectedPayee
     * @param selectedMonth
     * @param model
     * @return
     */
    public String createNewWrkData(String selectedPayee, String selectedMonth, Model model) {
 
        // ワークテーブル削除
        wrkDeleteService.deleteWrkDataByYearMonth(selectedMonth);
        // ワークテーブル作成
        wrkCreateService.createWrkDataFromMaster(selectedPayee, selectedMonth);
        model.addAttribute("message", messageSource.getMessage("newTable",new String[]{}, Locale.getDefault()));

        // CSV管理表に表示
        return wrkDisplayController.displayWrkData(selectedMonth, model);
    }

}
