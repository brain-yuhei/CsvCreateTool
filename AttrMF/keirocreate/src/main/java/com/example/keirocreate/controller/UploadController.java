package com.example.keirocreate.controller;

import com.example.keirocreate.model.WrkKeiroEntity;
import com.example.keirocreate.repository.WrkKeiroRepository;
import com.example.keirocreate.service.CsvService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;
import java.time.YearMonth;
import java.util.List;
import java.util.stream.Collectors;

@Controller
public class UploadController {

    @Autowired
    private CsvService csvService;

    @Autowired
    private WrkKeiroRepository wrkKeiroRepository;

    //アップロード画面を表示 ＋ データ表示
    @GetMapping("/upload")
    public String uploadPage(Model model) {
        List<WrkKeiroEntity> wrkList = wrkKeiroRepository.findAll();
        model.addAttribute("wrkList", wrkList);
        return "createkeiro"; 
    }

    //CSVアップロード処理
    @PostMapping("/upload")
    public String uploadCsv(@RequestParam("file") MultipartFile file,
                            @RequestParam("selectedMonth") String selectedMonth,
                             Model model) {
        try {
            // CSVをDBに保存（マスタ→ワークの流れ含む）
            csvService.saveCsvToDatabase(file);

            // 選択年月から月初・月末を計算
            YearMonth yearMonth = YearMonth.parse(selectedMonth);
            LocalDate startDate = yearMonth.atDay(1);
            LocalDate endDate = yearMonth.atEndOfMonth();

            // 日付リスト生成
            List<LocalDate> datesInMonth = startDate.datesUntil(endDate.plusDays(1)).collect(Collectors.toList());
            model.addAttribute("dateList", datesInMonth);

            // ワークテーブルから全データ取得（後で日付で絞れるならフィルタしてもOK）
            List<WrkKeiroEntity> wrkList = wrkKeiroRepository.findAll();
            model.addAttribute("wrkList", wrkList);
            model.addAttribute("currentTime", selectedMonth);
            model.addAttribute("message", "アップロード成功");

        } catch (Exception e) {
            model.addAttribute("message", "アップロード失敗: " + e.getMessage());
        }
        return "createkeiro";
    }

    //出力内容確認
    @GetMapping("/viewkeiro")
    public String viewKeiro(Model model) {
        List<WrkKeiroEntity> wrkList = wrkKeiroRepository.findAll();
        model.addAttribute("wrkList", wrkList);
        return "createkeiro";  
    }
}

