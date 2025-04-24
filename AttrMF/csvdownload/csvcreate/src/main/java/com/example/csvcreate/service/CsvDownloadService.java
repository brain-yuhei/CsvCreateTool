package com.example.csvcreate.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.csvcreate.model.WrkKeiroEntity;
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
    private WrkKeiroRepository wrkKeiroRepository;

    /**
     * ワークテーブルのデータ一覧を取得
     * 
     * @return ワークテーブル内の全データ
     */
    public List<WrkKeiroEntity> getWrkList() {
        return wrkKeiroRepository.findAll();
    }

    /**
     * 支払先・内容データの一覧を取得
     * 
     * @return 支払先・内容データ一覧
     */
    public List<String> getSelectedPayees() {
        return wrkKeiroRepository.findAll().stream()
                .map(WrkKeiroEntity::getPayee)
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
     * 日付ごとのデータリストと日付・曜日・チェック情報を返す
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
        WrkKeiroEntity baseRow = wrkKeiroRepository.findByPayee(selectedPayee).get(0);

        // 各日付に対して同様の内容を生成
        List<WrkKeiroEntity> repeatedWrkList = datesInMonth.stream().map(date -> {
            WrkKeiroEntity copy = new WrkKeiroEntity();
            copy.setPayee(baseRow.getPayee());
            copy.setExpenseCategory(baseRow.getExpenseCategory());
            copy.setAmount(baseRow.getAmount());
            copy.setMemo(baseRow.getMemo());
            copy.setDepartmentName(baseRow.getDepartmentName());
            copy.setDepartmentCode(baseRow.getDepartmentCode());
            copy.setDate(date); 
            return copy;
        }).collect(Collectors.toList());

        // 結果マップにリストを格納して返却
        result.put("wrkList", repeatedWrkList);
        result.put("dateInfoList", dateInfoList);
        return result;
    }
}

