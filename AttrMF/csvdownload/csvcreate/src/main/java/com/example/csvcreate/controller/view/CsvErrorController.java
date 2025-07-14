package com.example.csvcreate.controller.view;

import java.util.Locale;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class CsvErrorController {
   
    @Autowired
    private MessageSource messageSource;    

    /**
     * URL実行時の処理
     * 
     * @return
     */
    @GetMapping(value = {"/csvDeleteHistory", "/selectPayee", "/saveWorkTable","/createMasterTable"})
    public String redirectToError() {
        return "redirect:/csvError";
    }

    /**
     * 不正操作エラー画面の表示処理
     * 
     * @param model
     * @return
     */
    @GetMapping("/csvError")
    public String showInvalidPage(Model model) {
        model.addAttribute("errormessage", messageSource.getMessage("showInvalidPageError",new String[]{}, Locale.getDefault()));
        return "csvError"; 
    }

}
