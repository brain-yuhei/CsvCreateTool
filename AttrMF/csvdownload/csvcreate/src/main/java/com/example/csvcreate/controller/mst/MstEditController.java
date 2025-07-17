package com.example.csvcreate.controller.mst;

import java.util.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.csvcreate.model.MstKeiroEntity;
import com.example.csvcreate.model.MstKeiroFormDto;
import com.example.csvcreate.service.mst.SaveService;

@Controller
public class MstEditController {

    @Autowired
    private SaveService saveService;

    @Autowired
    private MessageSource messageSource;
    
    /**
     * 更新ボタン押下時の処理
     * 
     * @param formDto
     * @param model
     * @return
     */
    @PostMapping("/viewMasterTable")
    public String saveMasterTable(@ModelAttribute MstKeiroFormDto formDto, RedirectAttributes redirectAttributes, Model model) {
    
        // 全データを取得
        List<MstKeiroEntity> allRows = formDto.getMstKeiroList();       
    
        // チェックが入っている行だけを抽出
        List<MstKeiroEntity> checked = new ArrayList<>();
        for (MstKeiroEntity row : allRows) {
            if (Boolean.TRUE.equals(row.getSelected())) { 
                checked.add(row);
            }
        }

        // 未チェックの確認
        if (checked == null || checked.isEmpty()) {
            redirectAttributes.addFlashAttribute("errormessage", messageSource.getMessage("checkedError",new String[]{}, Locale.getDefault()));
            return "redirect:/viewMasterTable"; 
        } 
    
        // チェックされたデータのみを保存
        saveService.saveMstKeiroData(checked);
    
        model.addAttribute("mstdataList", allRows); 
        model.addAttribute("message", messageSource.getMessage("mstEdit",new String[]{}, Locale.getDefault()));
        return "mstTable";
    }   

}
