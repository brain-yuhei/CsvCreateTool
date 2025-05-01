package com.example.csvcreate.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.csvcreate.model.MstKeiroEntity;
import com.example.csvcreate.model.WrkKeiroEntity;
import com.example.csvcreate.repository.MstKeiroRepository;
import com.example.csvcreate.repository.WrkKeiroRepository;

import java.time.LocalDate;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

/**
 * CSVダウンロードに関するビジネスロジックを提供するサービスクラス
 */
@Service
public class CsvDownloadService {

    @Autowired
    private MstKeiroRepository mstKeiroRepository;

    @Autowired
    private WrkKeiroRepository wrkKeiroRepository;

    /**
     * マスタテーブルの全データを取得する
     *
     * @return マスタテーブルのリスト
     */
    public List<MstKeiroEntity> getMstList() {
        return mstKeiroRepository.findAll();
    }

    /**
     * マスタテーブルから「支払先・内容（payeeContent）」の一覧を取得する
     * nullや空文字を除外し、重複を排除する
     *
     * @return 支払先・内容のリスト
     */
    public List<String> getSelectedPayees() {
        return mstKeiroRepository.findAll().stream()
                .map(MstKeiroEntity::getPayeeContent)
                .filter(payee -> payee != null && !payee.isEmpty())
                .distinct()
                .collect(Collectors.toList());
    }

    /**
     * 現在の年月（yyyy-MM形式）を返す
     *
     * @return 現在の年月文字列
     */
    public String getNowYearFormat() {
        return YearMonth.now().format(DateTimeFormatter.ofPattern("yyyy-MM"));
    }

    /**
     * 現在から2ヶ月先までの年月の範囲を返す
     *
     * @return 「minMonth」と「maxMonth」をキーとする年月範囲マップ
     */
    public Map<String, String> getMonthRange() {
        YearMonth nowMonth = YearMonth.now();
        YearMonth twoMonthsLater = nowMonth.plusMonths(2);
        DateTimeFormatter ymFormatter = DateTimeFormatter.ofPattern("yyyy-MM");

        Map<String, String> result = new HashMap<>();
        result.put("minMonth", nowMonth.format(ymFormatter));
        result.put("maxMonth", twoMonthsLater.format(ymFormatter));
        return result;
    }

    /**
     * 支払先と年月に対応するデータを取得する
     * 必要であればワークテーブルにデータを生成してから取得する
     *
     * @param selectedPayee 選択された支払先
     * @param selectedMonth 選択された年月（yyyy-MM形式）
     * @return 表示用データ（ワークテーブルリストと日付リスト）を含むMap
     */
    public Map<String, Object> getPayeeAndMonth(String selectedPayee, String selectedMonth) {
        // ログ出力: メソッド開始時
        System.out.println("Getting data for Payee: " + selectedPayee + " and Month: " + selectedMonth);
    
        // ① ワークテーブルにデータがなければ作成
        createWrkDataIfNotExists(selectedPayee, selectedMonth);
    
        // ② 表示用データを取得
        List<WrkKeiroEntity> wrkList = wrkKeiroRepository.findByPayeeAndMonth(selectedPayee, selectedMonth);
        
        // ログ出力: データ取得後
        System.out.println("Work Table Data Retrieved: " + wrkList);
    
        List<LocalDate> dateInfoList = generateDateList(selectedMonth);
        
        // 結果を返す
        Map<String, Object> result = new HashMap<>();
        result.put("wrkList", wrkList);
        result.put("dateInfoList", dateInfoList);
        
        return result;
    }
    

    /**
     * 指定された支払先と年月に対して、ワークテーブルにデータがなければ作成する
     *
     * @param payeeContent 支払先・内容
     * @param yearMonthStr 年月（yyyy-MM形式）
     */
    private void createWrkDataIfNotExists(String payeeContent, String yearMonthStr) {
        // 既存データの存在チェック
        List<WrkKeiroEntity> existing = wrkKeiroRepository.findByPayeeAndMonth(payeeContent, yearMonthStr);
        if (!existing.isEmpty()) {
            return; // すでに存在していれば何もしない
        }

        // マスタデータ取得（支払先に一致する1件）
        Optional<MstKeiroEntity> mstOpt = mstKeiroRepository.findByPayeeContent(payeeContent);
        if (mstOpt.isEmpty()) {
            return; // マスタに該当なし
        }

        MstKeiroEntity mst = mstOpt.get();

        // 月の日付リストを作成（1日〜末日）
        List<LocalDate> dateList = generateDateList(yearMonthStr);

        for (LocalDate date : dateList) {
            WrkKeiroEntity wrk = new WrkKeiroEntity();
            wrk.setDate(date);
            wrk.setPayee(payeeContent);
            wrk.setExpenseCategory(mst.getExpense_category());
            wrk.setAmount(mst.getAmountInclusiveTax());
            wrk.setMemo(mst.getMemo()); 
            wrk.setDepartmentName(mst.getDepartment_name());
            wrk.setDepartmentCode(mst.getDepartment_code());

            wrkKeiroRepository.save(wrk);
        }
    }

    /**
     * 指定された年月（yyyy-MM）の日付リストを生成する
     *
     * @param yearMonthStr 年月（yyyy-MM）
     * @return LocalDate のリスト（1日〜末日）
     */
    private List<LocalDate> generateDateList(String yearMonthStr) {
        YearMonth ym = YearMonth.parse(yearMonthStr);
        List<LocalDate> dates = new ArrayList<>();
        for (int day = 1; day <= ym.lengthOfMonth(); day++) {
            dates.add(ym.atDay(day));
        }
        return dates;
    }

}


