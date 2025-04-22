package com.example.csvcreate.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import com.example.csvcreate.service.CsvService;

@Controller
public class CsvuploadController {

    @Autowired
    private CsvService csvService;

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
     * CSVファイルのアップロード処理
     * 
     * @param uploadfile CSVファイル
     * @param model
     * @return CSVファイルのアップロード結果
     */
    @PostMapping("/csvUpload")
    public String uploadFile(@RequestParam("uploadfile") MultipartFile uploadfile, Model model) {
        try {
            // CSVファイルを読み込み、DBに保存する
            csvService.saveDatabase(uploadfile);
            model.addAttribute("message", "アップロードに成功しました！");
        } catch (Exception e) {
            // エラー時はエラーメッセージを表示
            model.addAttribute("message", "アップロードに失敗しました: " + e.getMessage());
        }

        return "redirect:/currentMonth"; 
    }
}

