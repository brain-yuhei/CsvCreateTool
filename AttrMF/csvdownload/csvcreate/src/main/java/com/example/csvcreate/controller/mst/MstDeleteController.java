package com.example.csvcreate.controller.mst;

import java.util.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.csvcreate.service.mst.DeleteService;

@Controller
public class MstDeleteController {

    @Autowired
    private DeleteService deleteService;

    @Autowired
    private MessageSource messageSource;

    /**
     * 削除ボタン押下時の処理
     * 
     * @param id
     * @return
     */
    @PostMapping("/mstdelete")
    public String delete(@RequestParam("id") Long id, Model model) {

        // 削除処理を呼び出す
        deleteService.deleteById(id);
        // リダイレクトでもメッセージを返せるように考える
        // model.addAttribute("message", messageSource.getMessage("mstdelete",new String[]{}, Locale.getDefault()));
        return "redirect:/viewMasterTable"; 
    }

    /**
     * 一括削除処理
     * 
     * @param ids
     * @param model
     * @return
     */
    @PostMapping("/mstdelete/checked")
    public String checkDelete(@RequestParam(name = "ids", required = false) List<Long> ids, Model model) {

        // 未チェックの確認
        if (ids == null || ids.isEmpty()) {
            model.addAttribute("errormessage", messageSource.getMessage("checkedError",new String[]{}, Locale.getDefault()));
            return "mstTable"; 
        }

        // 削除処理を呼び出す
        deleteService.deleteByAllIds(ids);
        return "redirect:/viewMasterTable";
    }

}
