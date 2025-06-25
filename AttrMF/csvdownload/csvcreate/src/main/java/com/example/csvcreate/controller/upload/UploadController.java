package com.example.csvcreate.controller.upload;

import org.springframework.stereotype.Controller;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.example.csvcreate.service.CsvImportService;

@Controller
public class UploadController {

    @Autowired
    private CsvImportService csvImportService; 

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
            // ファイル名が空、または .csv でない場合はエラー
            String filename = uploadfile.getOriginalFilename();
            if (filename == null || !filename.toLowerCase().endsWith(".csv")) {
                model.addAttribute("errormessage", "CSVファイルを選択してください（拡張子が .csv である必要があります）");
                return "csvUpload"; 
            }
    
            // CSVファイルを読み込み、DBに保存する
            csvImportService.saveDatabase(uploadfile);
            model.addAttribute("message", "アップロードに成功しました！");
    
        } catch (Exception e) {
            model.addAttribute("errormessage", "アップロードに失敗しました：CSVファイルの項目数が不足しています " + e.getMessage());
            return "csvUpload"; 
        }
    
        return "redirect:/currentMonth"; 
    }    
    
}
