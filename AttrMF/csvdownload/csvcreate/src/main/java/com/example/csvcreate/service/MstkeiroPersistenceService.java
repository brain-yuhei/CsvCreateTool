package com.example.csvcreate.service;
import java.util.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import com.example.csvcreate.model.MstKeiroEntity;
import com.example.csvcreate.repository.MstKeiroRepository;
import org.springframework.stereotype.Service;

@Service
public class MstkeiroPersistenceService {

    @Autowired
    private MstKeiroRepository mstKeiroRepository;

    /**
     * マスタテーブルにデータを保存処理
     * 
     * @param newMstList 登録経路一覧表
     */
    @Transactional
    public void saveMstKeiroData(List<MstKeiroEntity> newMstList) {

        
        for (MstKeiroEntity newEntry : newMstList) {

            Optional<MstKeiroEntity> existing = mstKeiroRepository.findById(newEntry.getId());

            MstKeiroEntity entityToSave = existing.orElse(new MstKeiroEntity());
            entityToSave.setId(newEntry.getId()); 
            entityToSave.setDate(newEntry.getDate());
            entityToSave.setPayeeContent(newEntry.getPayeeContent());
            entityToSave.setAmountInclusiveTax(newEntry.getAmountInclusiveTax());
            entityToSave.setExpense_category(newEntry.getExpense_category());
            entityToSave.setMemo(newEntry.getMemo());
            entityToSave.setDepartment_name(newEntry.getDepartment_name());
            entityToSave.setDepartment_code(newEntry.getDepartment_code());

            mstKeiroRepository.save(entityToSave);
        }
    }

    
}
