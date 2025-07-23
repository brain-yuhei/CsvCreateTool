package com.example.csvcreate.controller.view;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.csvcreate.controller.wrk.WrkDisplayController;

@Controller
public class CsvDeleteHistoryController {

    @Autowired
    private WrkDisplayController wrkDisplayController;

    /**
     * 
     * 
     * @param selectedPayee
     * @param selectedMonth
     * @param model
     * @return
     */
    @PostMapping("/csvHistoryDisplay")
    public String csvHistoryDisplay(@RequestParam("selectedMonth") String selectedMonth,
                                     Model model) {
                               
        return wrkDisplayController.showHistoryData(selectedMonth, model);
    }


}
