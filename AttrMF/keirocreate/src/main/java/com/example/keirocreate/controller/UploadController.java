package com.example.keirocreate.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.keirocreate.service.CsvService;
import org.springframework.web.multipart.MultipartFile;


@Controller
public class UploadController {

    @Autowired
    private CsvService csvService;

    @GetMapping("/upload")
    public String uploadPage() {
        return "createkeiro"; 
    }

    /**
     * 
     * @param file
     * @param model
     * @return
     */
    @PostMapping("/upload")
    public String uploadCsv(@RequestParam("file") MultipartFile file, Model model) {
        try {
            csvService.saveCsvToDatabase(file);
            model.addAttribute("message", "アップロード成功");
        } catch (Exception e) {
            model.addAttribute("message", "アップロード失敗: " + e.getMessage());
        }
        return "createkeiro";
    }
}

