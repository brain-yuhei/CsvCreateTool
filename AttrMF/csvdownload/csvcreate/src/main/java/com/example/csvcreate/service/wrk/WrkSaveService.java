package com.example.csvcreate.service.wrk;

import java.util.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.csvcreate.model.WrkKeiroEntity;
import com.example.csvcreate.repository.WrkKeiroRepository;

@Service
public class WrkSaveService {
  
    @Autowired
    private WrkKeiroRepository wrkKeiroRepository;    

    /**
     * CSV管理表のデータを保存処理
     *
     * @param newList 保存対象の交通費リスト
     */
    @Transactional
    public void saveWrkKeiroData(List<WrkKeiroEntity> newList) {
    
        if (newList == null || newList.isEmpty()) return;
    
        for (WrkKeiroEntity newEntry : newList) {
    
            WrkKeiroEntity entityToSave = new WrkKeiroEntity();
    
            entityToSave.setDate(newEntry.getDate());
            entityToSave.setPayee(newEntry.getPayee());
            entityToSave.setExpenseCategory(newEntry.getExpenseCategory());
            entityToSave.setAmount(newEntry.getAmount());
            entityToSave.setMemo(newEntry.getMemo());
            entityToSave.setDepartmentName(newEntry.getDepartmentName());
            entityToSave.setDepartmentCode(newEntry.getDepartmentCode());
            entityToSave.setChecked(newEntry.getChecked());
    
            wrkKeiroRepository.save(entityToSave);
        }
    }    

}
