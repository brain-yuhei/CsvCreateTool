package com.example.csvcreate.service.api;

import java.time.LocalDate;
import java.time.YearMonth;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.csvcreate.service.wrk.WrkGetService;

@Service
public class GetDateService {

    @Autowired
    private WrkGetService wrkGetService;

    public boolean createDate(String selectedMonth){

        YearMonth yearMonth = YearMonth.parse(selectedMonth);
        LocalDate startDate = yearMonth.atDay(1);
        LocalDate endDate = yearMonth.atEndOfMonth();
        return wrkGetService.existsWrkDataByDateOnly(startDate, endDate);

    }
 
}
