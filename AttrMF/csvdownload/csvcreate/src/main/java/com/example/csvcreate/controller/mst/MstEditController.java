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
     * 更新ボタン押下時の処理
     * 
     * @param formDto
     * @param model
     * @return
     */
    @PostMapping("/viewMasterTable")
    public String saveMasterTable(@ModelAttribute MstKeiroFormDto formDto, Model model) {
    
        // 全データを取得
        List<MstKeiroEntity> allRows = formDto.getMstKeiroList();
    
        // チェックが入っている行だけを抽出
        List<MstKeiroEntity> checked = new ArrayList<>();
        for (MstKeiroEntity row : allRows) {
            if (Boolean.TRUE.equals(row.getSelected())) { 
                checked.add(row);
            }
        }
    
        // チェックされたデータのみを保存
        saveService.saveMstKeiroData(checked);
    
        model.addAttribute("mstdataList", allRows); 
        return "mstTable";
    }   

}
