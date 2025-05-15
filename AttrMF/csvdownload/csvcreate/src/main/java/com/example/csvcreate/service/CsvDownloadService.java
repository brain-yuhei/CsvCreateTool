package com.example.csvcreate.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.csvcreate.model.MstKeiroEntity;
import com.example.csvcreate.model.WrkKeiroEntity;
import com.example.csvcreate.repository.MstHolidayRepository;
import com.example.csvcreate.repository.MstKeiroRepository;
import com.example.csvcreate.repository.WrkKeiroRepository;

import java.nio.charset.StandardCharsets;
import java.text.Normalizer;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;
import org.springframework.transaction.annotation.Transactional;


@Service
public class CsvDownloadService {

    @Autowired
    private MstKeiroRepository mstKeiroRepository;

    @Autowired
    private WrkKeiroRepository wrkKeiroRepository;
    
    @Autowired
    private MstHolidayRepository mstHolidayRepository;

    /**
     * マスタテーブルのデータ一覧を取得
     * 
     * @return マスタテーブル内の全データ
     */
    public List<MstKeiroEntity> getMstList() {
        return mstKeiroRepository.findAll();
    }

    /**
     * 支払先・内容データの一覧を取得
     * 
     * @return 支払先・内容データ一覧
     */
    public List<String> getSelectedPayees() {
        return mstKeiroRepository.findAll().stream()
                .map(MstKeiroEntity::getPayeeContent)
                .filter(payee -> payee != null && !payee.isEmpty())
                .distinct()
                .collect(Collectors.toList());
    }

    /**
     * 現在の年月を返す
     * 
     * @return 現在の年月（yyyy-MM形式）
     */
    public String getNowYearFormat() {
        return YearMonth.now().format(DateTimeFormatter.ofPattern("yyyy-MM"));
    }

    /**
     * 2ヶ月後の範囲を返す
     * 
     * @return 年月の範囲（minMonth, maxMonth）
     */
    public Map<String, String> getMonthRange() {
        // 当年月のインスタンス生成
        YearMonth nowMonth = YearMonth.now();
        // 2カ月後年月のインスタンス生成
        YearMonth twoMonthsLater = nowMonth.plusMonths(2);
        // 年月のフォーマットを設定
        DateTimeFormatter ymFormatter = DateTimeFormatter.ofPattern("yyyy-MM");

        // 空箱を生成
        Map<String, String> result = new HashMap<>();
        result.put("minMonth", nowMonth.format(ymFormatter));
        result.put("maxMonth", twoMonthsLater.format(ymFormatter));
        return result;
    }

    /**
     * CSV管理表生成ボタン押下時の処理
     * 
     * @param selectedPayee
     * @param selectedMonth
     */
    public void createWorkTableData(String selectedPayee, String selectedMonth) {

        // 選択経路を使い、一致するマスタテーブルのデータ一覧を取得
        List<MstKeiroEntity> mstData = mstKeiroRepository.findByPayeeContent(selectedPayee);

        // 選択年月を変換（例：2025-05）
        YearMonth ym = YearMonth.parse(selectedMonth);
    
        // 空のワークテーブル一覧を生成
        List<WrkKeiroEntity> wrkToSave = new ArrayList<>();
    
        // 対象月を月末までループ
        for (int day = 1; day <= ym.lengthOfMonth(); day++) {

            // 年月日を生成
            LocalDate date = ym.atDay(day);

            // 選択経路と一致するマスタテーブルデータ分処理を行う
            for (MstKeiroEntity mst : mstData) {
    
                // 選択経路と年月日でワークテーブルにデータあるか確認
                Optional<WrkKeiroEntity> existing = wrkKeiroRepository.findByPayeeAndDate(selectedPayee, date);
    
                // すでにワークテーブルにデータがあればそれを利用し無ければ作成する
                WrkKeiroEntity wrk = existing.orElse(new WrkKeiroEntity());
                wrk.setDate(date);
                wrk.setPayee(mst.getPayeeContent());
                wrk.setExpenseCategory(mst.getExpense_category());
                wrk.setAmount(mst.getAmountInclusiveTax());
                wrk.setMemo(mst.getMemo());
                wrk.setDepartmentName(mst.getDepartment_name());
                wrk.setDepartmentCode(mst.getDepartment_code());
                wrkToSave.add(wrk);
            }
        }
    
        // ワークテーブルを保存
        wrkKeiroRepository.saveAll(wrkToSave);
    }
    
    

    // /**
    //  * CSV管理表へのデータ表示設定
    //  *
    //  * @param selectedPayee 選択された経路
    //  * @param selectedMonth 選択された年月
    //  * @return 日付ごとのデータリスト＆日付・曜日・チェック情報
    //  */
    // public Map<String, Object> getPayeeAndMonth(String selectedPayee, String selectedMonth) {

    //     // 空箱を生成
    //     Map<String, Object> result = new HashMap<>();

    //     // 選択年月を変換（例：2025-05）
    //     YearMonth yearMonth = YearMonth.parse(selectedMonth);

    //     // 月初と月末の日付を取得
    //     LocalDate startDate = yearMonth.atDay(1);
    //     LocalDate endDate = yearMonth.atEndOfMonth();

    //     // 月初から月末までの日付一覧を作成
    //     List<LocalDate> datesInMonth = startDate.datesUntil(endDate.plusDays(1)).collect(Collectors.toList());

    //     // 曜日を日本語に変換
    //     DateTimeFormatter formatter = DateTimeFormatter.ofPattern("E", Locale.JAPANESE);

    //     // 日付ごとに曜日とチェック有無一覧を生成
    //     List<Map<String, Object>> dateInfoList = datesInMonth.stream().map(date -> {

    //         // 空箱を生成し日付一覧を詰める
    //         Map<String, Object> map = new HashMap<>();
    //         map.put("date", date); 
    //         map.put("dayOfWeek", date.format(formatter)); 

    //         // 曜日を取得
    //         DayOfWeek dayOfWeek = date.getDayOfWeek();

    //         // 土日および祝日をチェック対象外にする
    //         boolean isWeekday = !(dayOfWeek == DayOfWeek.SATURDAY || dayOfWeek == DayOfWeek.SUNDAY);
    //         boolean isHoliday = mstHolidayRepository.existsByHolidayDate(date);

    //         // 平日かつ祝日でなければチェック一覧を詰める
    //         map.put("checked", isWeekday && !isHoliday);

    //         return map;
    //     }).collect(Collectors.toList());

    //     // 選択された支払先に該当する1件目のデータを取得
    //     MstKeiroEntity baseRow = mstKeiroRepository.findByPayeeContent(selectedPayee).get(0);

    //     // 各日付に対して同様の内容を生成
    //     List<MstKeiroEntity> repeatedWrkList = datesInMonth.stream().map(date -> {
    //         MstKeiroEntity copy = new MstKeiroEntity();
    //         copy.setPayeeContent(baseRow.getPayeeContent());
    //         copy.setExpense_category(baseRow.getExpense_category());
    //         copy.setAmountInclusiveTax(baseRow.getAmountInclusiveTax());
    //         copy.setMemo(baseRow.getMemo());
    //         copy.setDepartment_name(baseRow.getDepartment_name());
    //         copy.setDepartment_code(baseRow.getDepartment_code());
    //         copy.setDate(date); 
    //         return copy;
    //     }).collect(Collectors.toList());

    //     // 結果マップにリストを格納して返却
    //     result.put("wrkList", repeatedWrkList);
    //     result.put("dateInfoList", dateInfoList);
    //     return result;
    // } 

    @Transactional
    public void createWrkDataFromMaster(String selectedPayee, String selectedMonth) {

        // 選択年月で月初と月末を設定
        YearMonth yearMonth = YearMonth.parse(selectedMonth);
        LocalDate startDate = yearMonth.atDay(1);
        LocalDate endDate = yearMonth.atEndOfMonth();

        // 月初から月末までの日付一覧を作成
        List<LocalDate> datesInMonth = startDate.datesUntil(endDate.plusDays(1)).collect(Collectors.toList());

        // 選択された支払先に該当する1件目のデータを取得
        MstKeiroEntity baseRow = mstKeiroRepository.findByPayeeContent(selectedPayee).get(0);

        // 各日付に対して同様の内容を生成
        List<WrkKeiroEntity> wrkEntities = datesInMonth.stream().map(date -> {
            WrkKeiroEntity entity = new WrkKeiroEntity();
            entity.setPayee(baseRow.getPayeeContent()); 
            entity.setExpenseCategory(baseRow.getExpense_category());
            entity.setAmount(baseRow.getAmountInclusiveTax());
            entity.setMemo(baseRow.getMemo());
            entity.setDepartmentName(baseRow.getDepartment_name());
            entity.setDepartmentCode(baseRow.getDepartment_code());
            entity.setDate(date);
            return entity;
        }).collect(Collectors.toList());
    // ▼ ログ出力（確認用）
    System.out.println("=== 登録対象のWrkKeiroEntity一覧 ===");
    wrkEntities.forEach(e -> {
        System.out.println("日付: " + e.getDate()
            + ", 支払先: " + e.getPayee()
            + ", 金額: " + e.getAmount()
            + ", 部署コード: " + e.getDepartmentCode());
    });
        // リスト内のデータをワークテーブルに保存
        wrkKeiroRepository.saveAll(wrkEntities); 
    }

    public Map<String, Object> getWrkDataForDisplay(String selectedPayee, String selectedMonth) {

        // 選択年月で月初と月末を設定
        YearMonth yearMonth = YearMonth.parse(selectedMonth);
        LocalDate startDate = yearMonth.atDay(1);
        LocalDate endDate = yearMonth.atEndOfMonth();


        List<LocalDate> datesInMonth = startDate.datesUntil(endDate.plusDays(1)).collect(Collectors.toList());
    
        List<Map<String, Object>> dateInfoList = datesInMonth.stream().map(date -> {
            Map<String, Object> map = new HashMap<>();
            map.put("date", date);
            map.put("dayOfWeek", date.format(DateTimeFormatter.ofPattern("E", Locale.JAPANESE)));
            boolean isWeekday = !(date.getDayOfWeek() == DayOfWeek.SATURDAY || date.getDayOfWeek() == DayOfWeek.SUNDAY);
            boolean isHoliday = mstHolidayRepository.existsByHolidayDate(date);
            map.put("checked", isWeekday && !isHoliday);
            return map;
        }).collect(Collectors.toList());
    
        List<WrkKeiroEntity> wrkList = wrkKeiroRepository.findByDateBetweenAndPayee(startDate, endDate, selectedPayee);
        
        Map<String, Object> result = new HashMap<>();
        result.put("wrkList", wrkList);
        result.put("dateInfoList", dateInfoList);
        return result;
    }
        


// public Map<String, Object> getWrkTableData(String selectedPayee, String selectedMonth) {
//     Map<String, Object> result = new HashMap<>();

//     // 文字列正規化
//     selectedPayee = selectedPayee.trim();
//     selectedPayee = Normalizer.normalize(selectedPayee, Normalizer.Form.NFKC);

//     System.out.println("正規化後の支払先: [" + selectedPayee + "]");
//     System.out.println("バイト列: " + Arrays.toString(selectedPayee.getBytes(StandardCharsets.UTF_8)));

//     YearMonth yearMonth = YearMonth.parse(selectedMonth);
//     LocalDate startDate = yearMonth.atDay(1);
//     LocalDate endDate = yearMonth.atEndOfMonth();

//     List<WrkKeiroEntity> wrkList = wrkKeiroRepository.findByPayeeAndDateBetweenOrderByDate(selectedPayee, startDate, endDate);
//     if (wrkList == null) {
//         wrkList = new ArrayList<>();
//     }
//     System.out.println("取得件数：" + wrkList.size());

//     // 日付・曜日リスト生成
//     List<LocalDate> datesInMonth = startDate.datesUntil(endDate.plusDays(1)).collect(Collectors.toList());
//     DateTimeFormatter formatter = DateTimeFormatter.ofPattern("E", Locale.JAPANESE);

//     List<Map<String, Object>> dateInfoList = datesInMonth.stream().map(date -> {
//         Map<String, Object> map = new HashMap<>();
//         map.put("date", date);
//         map.put("dayOfWeek", date.format(formatter));
//         DayOfWeek dow = date.getDayOfWeek();
//         boolean isWeekday = !(dow == DayOfWeek.SATURDAY || dow == DayOfWeek.SUNDAY);
//         boolean isHoliday = mstHolidayRepository.existsByHolidayDate(date);
//         map.put("checked", isWeekday && !isHoliday);
//         return map;
//     }).collect(Collectors.toList());

//     result.put("wrkList", wrkList);
//     result.put("dateInfoList", dateInfoList);
//     return result;
// }

    
    

    /**
     * 
     * 
     * @param selectedMonth
     */
    public void deleteWrkData(String selectedMonth) {
        YearMonth ym = YearMonth.parse(selectedMonth);
        LocalDate startDate = ym.atDay(1);
        LocalDate endDate = ym.atEndOfMonth();
    
        wrkKeiroRepository.deleteByMonthRange(startDate, endDate);
    }
    
    /**
     * すでにワークテーブルが存在するか確認
     * 
     * @param selectedPayee
     * @param startDate
     * @param endDate
     * @return
     */
    public boolean existsWrkDataByDateOnly(LocalDate startDate, LocalDate endDate) {
        return wrkKeiroRepository.existsByDateBetween(startDate, endDate);
    }
    
        
    
}

