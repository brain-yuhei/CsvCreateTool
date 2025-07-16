package com.example.csvcreate.service.mst;

import java.util.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.csvcreate.model.MstCreateFormDto;
import com.example.csvcreate.model.MstKeiroEntity;
import com.example.csvcreate.repository.MstKeiroRepository;

@Service
public class SaveService {
 
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

    /**
     * 経路登録表のDB保存処理
     * 
     * @param dto 登録対象の値
     */
    public void saveMstDataForDisplay(MstCreateFormDto dto) {
        MstKeiroEntity entity = new MstKeiroEntity();

        // データをセット
        entity.setPayeeContent(dto.getPayeeContent());
        entity.setAmountInclusiveTax(dto.getAmountInclusiveTax());
        entity.setMemo(dto.getMemo());
        entity.setExpense_category(dto.getExpense_category());
        entity.setDepartment_name(dto.getDepartment_name());
        entity.setDepartment_code(dto.getDepartment_code());

        // マスタテーブルにデータを保存
        mstKeiroRepository.save(entity);
    }       

}
