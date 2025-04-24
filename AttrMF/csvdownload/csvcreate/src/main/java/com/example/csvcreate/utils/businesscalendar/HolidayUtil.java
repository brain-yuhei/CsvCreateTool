package com.example.csvcreate.utils.businesscalendar;

import java.time.LocalDate;

public class HolidayUtil {

    // 日付が祝日かどうかを判定
    public static boolean isHoliday(LocalDate date) {
        return Japan.getInstance().PUBLIC_HOLIDAYS.apply(date) != null;
    }

    // 祝日名を取得（なければ null）
    public static String getHolidayName(LocalDate date) {
        return Japan.getInstance().PUBLIC_HOLIDAYS.apply(date);
    }
}
