package com.example.csvcreate.controller;

// import java.time.LocalDate;
import java.util.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
// import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

// import com.example.csvcreate.model.WrkKeiroEntity;
// import com.example.csvcreate.repository.WrkKeiroRepository;
import com.example.csvcreate.service.CsvDownloadService;
// import com.example.csvcreate.model.KoutsuuhiFormWrapper;
// import com.example.csvcreate.model.KoutsuuhiFormItem;

@Controller
public class SaveWrkTableController {

    // @Autowired
    // private WrkKeiroRepository wrkKeiroRepository;

    @Autowired
    private CsvDownloadService csvDownloadService;

    // /selectPayee で選択された Payee と Month を受け取る処理
    @PostMapping("/generateWorkTable")
    public String generateWorkTable(@RequestParam String selectedPayee, 
                                    @RequestParam String selectedMonth, 
                                    Model model) {
    
        // ワークテーブル生成処理をサービスに委譲
        Map<String, Object> payeeAndMonthData = csvDownloadService.getPayeeAndMonth(selectedPayee, selectedMonth);
    
        // モデルに選択情報をセット
        model.addAttribute("selectedPayee", selectedPayee);
        model.addAttribute("selectedMonth", selectedMonth);
    
        // ワークテーブルのデータもセット
        model.addAttribute("wrkList", payeeAndMonthData.get("wrkList"));
        model.addAttribute("dateInfoList", payeeAndMonthData.get("dateInfoList"));

        return "csvDownload"; 
 
    }
    


    // /saveWorkTable でデータ保存を行う処理
    // @PostMapping("/saveWorkTable")
    // public String saveWorkTable(@RequestParam String selectedPayee, @RequestParam String selectedMonth, 
    //                             @ModelAttribute KoutsuuhiFormWrapper form, Model model) {

    //     // 入力されたデータ（支払内容リストとチェックされた日付）を取得
    //     List<KoutsuuhiFormItem> koutsuuhiList = form.getKoutsuuhiList();
    //     List<String> selectedDates = form.getSelectedDates();

    //     // チェックされた日付ごとに処理を行う
    //     for (String selectedDateStr : selectedDates) {
    //         LocalDate selectedDate = LocalDate.parse(selectedDateStr);

    //         // 対象の日付に一致する支払データを探す
    //         for (KoutsuuhiFormItem item : koutsuuhiList) {
    //             if (item == null || item.getDate() == null) {
    //                 continue; // nullはスキップ
    //             }

    //             LocalDate itemDate = LocalDate.parse(item.getDate());

    //             // チェックされた日付と支払先で一致する場合のみ保存
    //             if (itemDate.equals(selectedDate) && selectedPayee.equals(item.getPayeeContent())) {
    //                 WrkKeiroEntity existing = wrkKeiroRepository.findByDateAndPayee(itemDate, item.getPayeeContent());
    //                 WrkKeiroEntity csvdate = new WrkKeiroEntity();

    //                 if (existing != null) {
    //                     // 既存データありの場合は更新
    //                     csvdate = existing;   
    //                 } else {
    //                     // 無ければ新規作成
    //                     csvdate = new WrkKeiroEntity();
    //                     csvdate.setDate(itemDate);
    //                     csvdate.setPayee(item.getPayeeContent());
    //                 }

    //                 csvdate.setExpenseCategory(item.getExpense_category());
    //                 csvdate.setAmount(item.getAmountInclusiveTax());
    //                 csvdate.setMemo(item.getMemo());
    //                 csvdate.setDepartmentName(item.getDepartment_name());
    //                 csvdate.setDepartmentCode(item.getDepartment_code());

    //                 // データベースに保存
    //                 wrkKeiroRepository.save(csvdate);
    //             }
    //         }
    //     }
    //     List<WrkKeiroEntity> wrkList = wrkKeiroRepository.findByPayeeAndMonth(selectedPayee, selectedMonth);
    
    //     model.addAttribute("wrkList", wrkList);
    //     // 保存成功メッセージを渡して画面に戻る
    //     model.addAttribute("message", "ワークテーブルに保存しました");
    //     return "redirect:/currentMonth";
    // }
}



