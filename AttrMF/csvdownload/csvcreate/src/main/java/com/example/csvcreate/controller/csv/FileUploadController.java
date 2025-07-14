package com.example.csvcreate.controller.csv;

import java.util.Locale;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.example.csvcreate.service.CsvImportService;

@Controller
public class FileUploadController {
    
    @Autowired
    private CsvImportService csvImportService; 

    @Autowired
    private MessageSource messageSource;

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
                model.addAttribute("errormessage", messageSource.getMessage("csvFileError",new String[]{}, Locale.getDefault()));
                return "csvUpload"; 
            }
    
            // CSVファイルを読み込み、DBに保存する
            csvImportService.saveDatabase(uploadfile);
            model.addAttribute("message", messageSource.getMessage("uploadFile",new String[]{}, Locale.getDefault()));
    
        } catch (Exception e) {
            model.addAttribute("errormessage", messageSource.getMessage("uploadFileError",new String[]{}, Locale.getDefault()));
            return "csvUpload"; 
        }
    
        return "redirect:/currentMonth"; 
    } 

}
