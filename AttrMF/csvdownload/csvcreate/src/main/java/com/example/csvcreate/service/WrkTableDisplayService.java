package com.example.csvcreate.service;

import java.time.LocalDate;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.util.stream.Collectors;
import java.util.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.csvcreate.model.WrkKeiroEntity;
import com.example.csvcreate.repository.MstHolidayRepository;
import com.example.csvcreate.repository.WrkKeiroRepository;

@Service
public class WrkTableDisplayService {
    
    @Autowired
    private WrkKeiroRepository wrkKeiroRepository;

    @Autowired
    private MstHolidayRepository mstHolidayRepository;

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
        Map<LocalDate, List<WrkKeiroEntity>> wrkMap = wrkList.stream()
        .collect(Collectors.groupingBy(WrkKeiroEntity::getDate));
    
        // 日付情報（曜日＋チェック状態）
        List<Map<String, Object>> dateInfoList = startDate.datesUntil(endDate.plusDays(1))
            .map(date -> {
                Map<String, Object> map = new HashMap<>();
                map.put("date", date);
                map.put("displayDate", date.format(DateTimeFormatter.ofPattern("M/d")));
                map.put("dayOfWeek", date.format(DateTimeFormatter.ofPattern("E", Locale.JAPANESE)));
                map.put("dayOfWeekClass", getDayOfWeekClass(date));
    
                // ✅ ワークテーブルの checked を使う
                List<WrkKeiroEntity> entityList = wrkMap.get(date);
                boolean checked = (entityList != null && entityList.stream().anyMatch(e -> Boolean.TRUE.equals(e.getChecked())));
                map.put("checked", checked);
    
                return map;
            })
            .collect(Collectors.toList());
    
        Map<String, Object> result = new HashMap<>();
        result.put("wrkList", wrkList);
        result.put("dateInfoList", dateInfoList);
        return result;
    }
    
    /**
     * 曜日識別処理
     * 
     * @return
     */
    private String getDayOfWeekClass(LocalDate date) {
        if (mstHolidayRepository.existsByHolidayDate(date)) return "holiday";
    
        return switch (date.getDayOfWeek()) {
            case SATURDAY -> "saturday";
            case SUNDAY -> "sunday";
            default -> ""; // 平日は何もつけない
        };
    }

}
