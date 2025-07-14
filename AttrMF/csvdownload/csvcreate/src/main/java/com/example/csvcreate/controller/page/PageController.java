package com.example.csvcreate.controller.page;

import java.time.YearMonth;
import java.util.*;

import org.springframework.stereotype.Controller;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.csvcreate.model.MstKeiroEntity;
import com.example.csvcreate.service.DateService;
import com.example.csvcreate.service.MstKeiroService;
import com.example.csvcreate.service.MstTableDisplayService;



@Controller
public class PageController {

    @Autowired
    private MstKeiroService mstKeiroService; 
    
    @Autowired
    private DateService dateService;  
    
    @Autowired
    private MstTableDisplayService mstTableDisplayService;

    @Autowired
    private MessageSource messageSource;

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
     * CSVアップロード画面を表示する
     * 
     * @return アップロード画面
     */
    @GetMapping("/csvUpload")
    public String showUploadForm() {
        return "csvUpload";  
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
        List<String> payees = mstKeiroService.getSelectedPayees();
        model.addAttribute("selectedPayees", payees);
    
        if (selectedPayee == null || selectedPayee.isEmpty()) {
            selectedPayee = payees.stream().findFirst().orElse("");
        }
    
        // 年月・Payeeも再表示用に追加
        model.addAttribute("currentMonth", currentMonth);
        model.addAttribute("selectedPayee", selectedPayee);
    
        //年月の範囲を設定し最小月と最大月をモデルに追加
        Map<String, String> monthRange = dateService.getMonthRange();
        model.addAttribute("minMonth", monthRange.get("minMonth"));
        model.addAttribute("maxMonth", monthRange.get("maxMonth"));
    
        return "csvMenu"; // CSV管理表のJSP名
    }

    /**
     * URL実行時の処理
     * 
     * @return
     */
    @GetMapping(value = {"/csvDeleteHistory", "/selectPayee", "/saveWorkTable","/createMasterTable"})
    public String redirectToError() {
        return "redirect:/csvError";
    }
    
    /**
     * 
     * 
     * @param model
     * @return
     */
    @GetMapping("/csvError")
    public String showInvalidPage(Model model) {
        model.addAttribute("errormessage", messageSource.getMessage("showInvalidPageError",new String[]{}, Locale.getDefault()));
        return "csvError"; 
    }

    /**
     * 登録経路一覧画面を開いた際の処理
     * 
     * @param entity
     * @return
     */
    @GetMapping("/viewMasterTable")
    public String showMstTablePage(Model model) {

        // マスタテーブルのデータ取得処理
        List<MstKeiroEntity> mstdataList = mstTableDisplayService.getMstDataForDisplay();

        model.addAttribute("mstdataList", mstdataList);
        return "mstTableview";
    }  

}
