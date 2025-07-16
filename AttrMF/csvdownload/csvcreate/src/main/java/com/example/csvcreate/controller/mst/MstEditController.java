package com.example.csvcreate.controller.mst;

import java.util.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import com.example.csvcreate.model.MstKeiroEntity;
import com.example.csvcreate.model.MstKeiroFormDto;
import com.example.csvcreate.service.mst.SaveService;

@Controller
public class MstEditController {

    @Autowired
    private SaveService saveService;
    
    /**
     *更新ボタン押下時の処理
     * 
     * @return
     */
    @PostMapping("/viewMasterTable")
    public String saveMasterTable(@ModelAttribute MstKeiroFormDto formDto, Model model) {

        // 登録経路一覧表のデータをリスト追加
        List<MstKeiroEntity> newMstList = formDto.getMstKeiroList();

        // 入力された値をマスタテーブルに保存する処理
        saveService.saveMstKeiroData(newMstList);

        model.addAttribute("mstdataList", newMstList);
        return "mstTable";
    }    

}
