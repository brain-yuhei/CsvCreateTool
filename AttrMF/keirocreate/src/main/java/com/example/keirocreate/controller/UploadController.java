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

    // CSVファイルの保存・変換ロジックを担当するサービス
    @Autowired
    private CsvService csvService;

    // ワークテーブルへのアクセス
    @Autowired
    private WrkKeiroRepository wrkKeiroRepository;

    /**
     * CSV管理表用のデータ取得メソッド
     * 
     * @param model ワークテーブルのデータを格納するコンテナ
     * @return CSVファイル簡易作成ツール画面
     */
    @GetMapping("/upload")
    public String uploadPage(Model model) {
        // ワークテーブルのデータを取得
        List<WrkKeiroEntity> wrkList = wrkKeiroRepository.findAll();
        // モデルに追加
        model.addAttribute("wrkList", wrkList);
        return "createkeiro"; 
    }

    /**
     * CSVファイルのアップロード処理
     * 
     * @param file CSVファイル
     * @param selectedMonth 年月
     * @param model
     * @return CSVファイル簡易作成ツール画面
     */
    @PostMapping("/upload")
    public String uploadCsv(@RequestParam("file") MultipartFile file,
                            @RequestParam("selectedMonth") String selectedMonth,
                            Model model) {
        try {
            // CSVファイルを読み込み、DB保存処理を呼び出す
            csvService.saveCsvToDatabase(file);

            // 選択された年月から月初と月末の日付を取得する
            YearMonth yearMonth = YearMonth.parse(selectedMonth); 
            LocalDate startDate = yearMonth.atDay(1); 
            LocalDate endDate = yearMonth.atEndOfMonth();

            // 月初から月末までの日付リストを作成する
            List<LocalDate> datesInMonth = startDate
                    .datesUntil(endDate.plusDays(1)) 
                    .collect(Collectors.toList());
            model.addAttribute("dateList", datesInMonth);

            // ワークテーブルから全データを取得 
            List<WrkKeiroEntity> wrkList = wrkKeiroRepository.findAll();

            // JISファイル用に属性付与
            model.addAttribute("wrkList", wrkList);
            model.addAttribute("currentTime", selectedMonth); 
            model.addAttribute("message", "アップロード成功");

        } catch (Exception e) {
            // エラーが発生した場合はメッセージ表示
            model.addAttribute("message", "アップロード失敗: " + e.getMessage());
        }

        // createkeiro.jsp を表示
        return "createkeiro";
    }

    /**
     * 出力内容確認
     * 
     * @param model
     * @return CSVファイル簡易作成ツール画面
     */
    @GetMapping("/viewkeiro")
    public String viewKeiro(Model model) {
        // ワークテーブルの全データを取得
        List<WrkKeiroEntity> wrkList = wrkKeiroRepository.findAll();
        // 画面に渡す
        model.addAttribute("wrkList", wrkList);
        return "createkeiro"; 
    }
}


