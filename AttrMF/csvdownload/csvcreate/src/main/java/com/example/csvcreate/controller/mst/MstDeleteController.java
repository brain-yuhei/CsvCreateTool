package com.example.csvcreate.controller.mst;

// import java.util.Locale;

import org.springframework.beans.factory.annotation.Autowired;
// import org.springframework.context.MessageSource;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.csvcreate.service.mst.DeleteService;

@Controller
public class MstDeleteController {

    @Autowired
    private DeleteService deleteService;

    // @Autowired
    // private MessageSource messageSource;

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
}
