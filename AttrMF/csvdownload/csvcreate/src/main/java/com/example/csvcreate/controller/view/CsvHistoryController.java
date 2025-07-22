package com.example.csvcreate.controller.view;

// import java.util.*;

// import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

// import com.example.csvcreate.service.wrk.WrkGetService;

import ch.qos.logback.core.model.Model;

@Controller
public class CsvHistoryController {

    // @Autowired
    // private WrkGetService wrkGetService;

    @GetMapping("/csvHistory")
    public String showCsvHistory(Model model) {

        // ワークテーブルから日付データを取得
        // List<String> historyMonth = wrkGetService.getHistoryYearMonth();

        // model.addAttribute("historyMonth", historyMonth);
        return "csvHistory"; 
    }

}
