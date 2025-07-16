package com.example.csvcreate.controller.mst;

import java.util.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import com.example.csvcreate.model.MstCreateFormDto;
import com.example.csvcreate.model.MstKeiroEntity;
import com.example.csvcreate.repository.MstKeiroRepository;
import com.example.csvcreate.service.mst.SaveService;

@Controller
public class MstCreateController {

    @Autowired
    MstKeiroRepository mstKeiroRepository; 

    @Autowired
    SaveService saveService;

    /**
     * 登録ボタン押下時の処理（経路登録画面）
     * 
     * @param dto 登録対象のデータ
     * @param model
     * @return
     */
    @PostMapping("/createMasterTable")
    public String saveMstTable(@ModelAttribute MstCreateFormDto dto, Model model) {

        // データ保存処理を呼び出し
        saveService.saveMstDataForDisplay(dto); 

        // DBから全件取得して表示用に渡す
        List<MstKeiroEntity> mstdataList = mstKeiroRepository.findAll();

        model.addAttribute("mstdataList", mstdataList);
        return "mstTable"; 
    }

}
