package com.example.csvcreate.controller.view;

import java.util.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.example.csvcreate.model.MstKeiroEntity;
import com.example.csvcreate.service.MstTableDisplayService;

@Controller
public class MstTableController {

    @Autowired
    private MstTableDisplayService mstTableDisplayService;    

    /**
     * 登録経路一覧画面を開いた際の処理
     * 
     * @param entity
     * @return
     */
    @GetMapping("/viewMasterTable")
    public String showMstTablePage(Model model) {

        // マスタテーブルのデータ取得処理
        List<MstKeiroEntity> mstdataList = mstTableDisplayService.getMstDataForDisplay();

        model.addAttribute("mstdataList", mstdataList);
        return "mstTable";
    }     

}
