package com.example.csvcreate.controller.view;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class CsvUploadController {
   
    /**
     * CSVアップロード画面を表示する
     * 
     * @return アップロード画面
     */
    @GetMapping("/csvUpload")
    public String showUploadForm() {
        return "csvUpload";  
    }    

}
