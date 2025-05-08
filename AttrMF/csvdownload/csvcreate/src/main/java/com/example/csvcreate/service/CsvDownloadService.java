package com.example.csvcreate.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.csvcreate.model.MstKeiroEntity;
import com.example.csvcreate.model.WrkKeiroEntity;
import com.example.csvcreate.repository.MstKeiroRepository;
import com.example.csvcreate.repository.WrkKeiroRepository;
import com.example.csvcreate.utils.businesscalendar.HolidayUtil;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class CsvDownloadService {

    @Autowired
    private MstKeiroRepository mstKeiroRepository;

    @Autowired
    private WrkKeiroRepository wrkKeiroRepository;    

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
    // マスタから対象の経路データを取得
    List<MstKeiroEntity> mstData = mstKeiroRepository.findByPayeeContent(selectedPayee);

    // 対象月の日付分、ワークテーブル用データを生成
    List<WrkKeiroEntity> wrkDataList = new ArrayList<>();
    YearMonth ym = YearMonth.parse(selectedMonth);
    for (int day = 1; day <= ym.lengthOfMonth(); day++) {
        LocalDate date = ym.atDay(day);

        for (MstKeiroEntity mst : mstData) {
            WrkKeiroEntity wrk = new WrkKeiroEntity();
            wrk.setDate(date);
            wrk.setPayee(mst.getPayeeContent());
            wrk.setExpenseCategory(mst.getExpense_category());
            wrk.setAmount(mst.getAmountInclusiveTax());
            wrk.setMemo(mst.getMemo());
            wrk.setDepartmentName(mst.getDepartment_name());
            wrk.setDepartmentCode(mst.getDepartment_code());
            wrk.setPayee(selectedPayee);
            // 他にも必要な情報をセット

            wrkDataList.add(wrk);
        }
    }

    // 上書き処理追加予定☆


    // 全データ保存
    wrkKeiroRepository.saveAll(wrkDataList);
}

    /**
     * CSV管理表へのデータ表示設定
     *
     * @param selectedPayee 選択された経路
     * @param selectedMonth 選択された年月
     * @return 日付ごとのデータリスト＆日付・曜日・チェック情報
     */
    public Map<String, Object> getPayeeAndMonth(String selectedPayee, String selectedMonth) {

        Map<String, Object> result = new HashMap<>();

        YearMonth yearMonth = YearMonth.parse(selectedMonth);

        // 月初と月末の日付を取得
        LocalDate startDate = yearMonth.atDay(1);
        LocalDate endDate = yearMonth.atEndOfMonth();

        // 月初から月末までの日付リストを作成
        List<LocalDate> datesInMonth = startDate.datesUntil(endDate.plusDays(1)).collect(Collectors.toList());

        // 曜日を日本語に変換
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("E", Locale.JAPANESE);

        // 日付ごとに曜日とチェック有無を生成
        List<Map<String, Object>> dateInfoList = datesInMonth.stream().map(date -> {
            Map<String, Object> map = new HashMap<>();
            map.put("date", date); 
            map.put("dayOfWeek", date.format(formatter)); 

            DayOfWeek dayOfWeek = date.getDayOfWeek();

            // 土日および祝日をチェック対象外にする
            boolean isWeekday = !(dayOfWeek == DayOfWeek.SATURDAY || dayOfWeek == DayOfWeek.SUNDAY);
            boolean isHoliday = HolidayUtil.isHoliday(date);
            map.put("checked", isWeekday && !isHoliday);

            return map;
        }).collect(Collectors.toList());

        // 選択された支払先に該当する1件目のデータを取得
        MstKeiroEntity baseRow = mstKeiroRepository.findByPayeeContent(selectedPayee).get(0);

        // 各日付に対して同様の内容を生成
        List<MstKeiroEntity> repeatedWrkList = datesInMonth.stream().map(date -> {
            MstKeiroEntity copy = new MstKeiroEntity();
            copy.setPayeeContent(baseRow.getPayeeContent());
            copy.setExpense_category(baseRow.getExpense_category());
            copy.setAmountInclusiveTax(baseRow.getAmountInclusiveTax());
            copy.setMemo(baseRow.getMemo());
            copy.setDepartment_name(baseRow.getDepartment_name());
            copy.setDepartment_code(baseRow.getDepartment_code());
            copy.setDate(date); 
            return copy;
        }).collect(Collectors.toList());

        // 結果マップにリストを格納して返却
        result.put("wrkList", repeatedWrkList);
        result.put("dateInfoList", dateInfoList);
        return result;
    }
}

