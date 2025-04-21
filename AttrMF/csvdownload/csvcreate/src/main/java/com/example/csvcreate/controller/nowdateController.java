package com.example.csvcreate.controller;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class NowdateController {

    /**
     * 現在時刻を取得しCSVファイル簡易作成ツール画面に表示する
     * 
     * @param model 現在時刻を格納
     * @return CSVファイル簡易作成ツール画面（createkeiro.jsp）
     */
    @GetMapping("/currentMonth")
    public String getKeiroDate(Model model) {
        // 現在時刻の取得
        final var now = LocalDate.now(); 
        // 現在時刻の設定
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM");
        // 取得した時刻に対して設定を適応
        String formattedDate = now.format(formatter); 
        // JSPファイルへの追加設定
        model.addAttribute("currentMonth", formattedDate); 
        // CSVファイル簡易作成ツール画面を返す      
        return "csvDownload";
    }
}
