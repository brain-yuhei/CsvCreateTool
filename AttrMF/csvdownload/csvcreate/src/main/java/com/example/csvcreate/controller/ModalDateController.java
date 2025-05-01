package com.example.csvcreate.controller;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.stereotype.Controller;

import com.example.csvcreate.model.KoutsuuhiFormItem;
import com.example.csvcreate.service.ModalDateService;

import java.util.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;

@Controller
public class ModalDateController {

    @Autowired
    private ModalDateService modalDateService;

    @PostMapping("/confirmSelection")
    public String confirmSelectedDates(@RequestParam("selectedDates") List<String> selectedDates, Model model) {
        System.out.println("選択された日付：" + selectedDates);
        List<KoutsuuhiFormItem> selectedItems = modalDateService.findByDates(selectedDates);
        System.out.println("選択されたアイテム数：" + selectedItems.size());
        model.addAttribute("modalDataList", selectedItems);  // モーダルデータをセット
        model.addAttribute("showModal", true);  // モーダルを表示するためのフラグ
        return "csvDownload";  // JSPに戻る
    }
       
}
