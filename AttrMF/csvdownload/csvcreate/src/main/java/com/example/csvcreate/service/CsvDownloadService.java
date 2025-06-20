package com.example.csvcreate.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.csvcreate.model.MstKeiroEntity;
import com.example.csvcreate.model.WrkKeiroEntity;
import com.example.csvcreate.repository.MstHolidayRepository;
import com.example.csvcreate.repository.MstKeiroRepository;
import com.example.csvcreate.repository.WrkKeiroRepository;

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
        // 1カ月前の年月のインスタンス生成
        YearMonth oneMonthAgo = YearMonth.now().minusMonths(1);
        // 2カ月後年月のインスタンス生成
        YearMonth twoMonthsLater = nowMonth.plusMonths(2);
        // 年月のフォーマットを設定
        DateTimeFormatter ymFormatter = DateTimeFormatter.ofPattern("yyyy-MM");

        // 空箱を生成
        Map<String, String> result = new HashMap<>();
        result.put("minMonth", oneMonthAgo.format(ymFormatter));
        result.put("nowMonth", nowMonth.format(ymFormatter));
        result.put("maxMonth", twoMonthsLater.format(ymFormatter));
        return result;
    }

    /**
     * ワークテーブルデータを生成処理
     * 
     * @param selectedPayee
     * @param selectedMonth
     */
    @Transactional
    public void createWrkDataFromMaster(String selectedPayee, String selectedMonth) {
    
        YearMonth yearMonth = YearMonth.parse(selectedMonth);
        LocalDate startDate = yearMonth.atDay(1);
        LocalDate endDate = yearMonth.atEndOfMonth();
    
        List<LocalDate> datesInMonth = startDate.datesUntil(endDate.plusDays(1)).collect(Collectors.toList());
    
        MstKeiroEntity baseDate = mstKeiroRepository.findByPayeeContent(selectedPayee).get(0);
    
        for (LocalDate date : datesInMonth) {
            WrkKeiroEntity entity = new WrkKeiroEntity();
    
            entity.setPayee(baseDate.getPayeeContent());
            entity.setExpenseCategory(baseDate.getExpense_category());
            entity.setAmount(baseDate.getAmountInclusiveTax());
            entity.setMemo(baseDate.getMemo());
            entity.setDepartmentName(baseDate.getDepartment_name());
            entity.setDepartmentCode(baseDate.getDepartment_code());
            entity.setDate(date);
    
            // ✅ チェック状態の初期登録
            boolean isWeekday = !(date.getDayOfWeek() == DayOfWeek.SATURDAY || date.getDayOfWeek() == DayOfWeek.SUNDAY);
            boolean isHoliday = mstHolidayRepository.existsByHolidayDate(date);
            entity.setChecked(isWeekday && !isHoliday); // ← チェック状態をセット
    
            wrkKeiroRepository.save(entity);
        }
    }
    

    @Transactional
    public void deleteWrkDataByYearMonth(String selectedMonth) {
        YearMonth yearMonth = YearMonth.parse(selectedMonth);
        LocalDate startDate = yearMonth.atDay(1);
        LocalDate endDate = yearMonth.atEndOfMonth();
    
        wrkKeiroRepository.deleteByDateBetween(startDate, endDate);
    }
    

    /**
     * ワークテーブルからCSV管理表に出力用のデータ取得処理
     * 
     * @param selectedPayee
     * @param selectedMonth
     * @return
     */
    public Map<String, Object> getWrkDataForDisplay(String selectedPayee, String selectedMonth) {

        YearMonth yearMonth = YearMonth.parse(selectedMonth);
        LocalDate startDate = yearMonth.atDay(1);
        LocalDate endDate = yearMonth.atEndOfMonth();
    
        // ワークテーブルから対象データ取得
        List<WrkKeiroEntity> wrkList = wrkKeiroRepository.findByDateBetween(startDate, endDate);
    
        // date -> WrkKeiroEntity のマップを作成
        Map<LocalDate, WrkKeiroEntity> wrkMap = wrkList.stream()
            .collect(Collectors.toMap(WrkKeiroEntity::getDate, e -> e));
    
        // 日付情報（曜日＋チェック状態）
        List<Map<String, Object>> dateInfoList = startDate.datesUntil(endDate.plusDays(1))
            .map(date -> {
                Map<String, Object> map = new HashMap<>();
                map.put("date", date);
                map.put("dayOfWeek", date.format(DateTimeFormatter.ofPattern("E", Locale.JAPANESE)));
    
                // ✅ ワークテーブルの checked を使う
                WrkKeiroEntity entity = wrkMap.get(date);
                map.put("checked", entity != null ? Boolean.TRUE.equals(entity.getChecked()) : false);
    
                return map;
            })
            .collect(Collectors.toList());
    
        Map<String, Object> result = new HashMap<>();
        result.put("wrkList", wrkList);
        result.put("dateInfoList", dateInfoList);
        return result;
    } 
}

