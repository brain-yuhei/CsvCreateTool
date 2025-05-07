package com.example.csvcreate.controller;

import java.time.LocalDate;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import com.example.csvcreate.model.WrkKeiroEntity;
import com.example.csvcreate.repository.WrkKeiroRepository;
import com.example.csvcreate.model.KoutsuuhiFormWrapper;
import com.example.csvcreate.model.KoutsuuhiFormItem;

@Controller
public class SaveWrkTableController {

    @Autowired
    private WrkKeiroRepository wrkKeiroRepository;

    @PostMapping("/saveWorkTable")
    public String saveWorkTable(@ModelAttribute KoutsuuhiFormWrapper form, Model model) {

        // 入力されたデータ（支払内容リストとチェックされた日付）を取得
        List<KoutsuuhiFormItem> koutsuuhiList = form.getKoutsuuhiList();
        List<String> selectedDates = form.getSelectedDates();

        // チェックされた日付ごとに処理を行う
        for (String selectedDateStr : selectedDates) {
            LocalDate selectedDate = LocalDate.parse(selectedDateStr);

            // 対象の日付に一致する支払データを探す
            for (KoutsuuhiFormItem item : koutsuuhiList) {
                if (item == null || item.getDate() == null) {
                    continue; // nullはスキップ
                }

                LocalDate itemDate = LocalDate.parse(item.getDate());

                // チェックされた日付と支払先で一致する場合のみ保存
                if (itemDate.equals(selectedDate)) {
                    WrkKeiroEntity existing = wrkKeiroRepository.findByDateAndPayee(itemDate, item.getPayeeContent());
                    WrkKeiroEntity csvdate = new WrkKeiroEntity();

                    if (existing != null) {
                        // 既存データありの場合は更新
                        csvdate = existing;   
                    }else{
                        // 無ければ新規作成
                        csvdate = new WrkKeiroEntity();
                        csvdate.setDate(itemDate);
                        csvdate.setPayee(item.getPayeeContent());
                    }

                    csvdate.setPayee(item.getPayeeContent());
                    csvdate.setExpenseCategory(item.getExpense_category());
                    csvdate.setAmount(item.getAmountInclusiveTax());
                    csvdate.setMemo(item.getMemo());
                    csvdate.setDepartmentName(item.getDepartment_name());
                    csvdate.setDepartmentCode(item.getDepartment_code());

                    // データベースに保存
                    wrkKeiroRepository.save(csvdate);
                }
            }
        }

        // 保存成功メッセージを渡して画面に戻る
        model.addAttribute("message", "ワークテーブルに保存しました");
        return "redirect:/currentMonth";
    }
}


