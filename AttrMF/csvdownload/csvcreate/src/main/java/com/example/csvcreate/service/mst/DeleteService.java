package com.example.csvcreate.service.mst;

import java.util.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.csvcreate.repository.MstKeiroRepository;

@Service
public class DeleteService {
 
    @Autowired
    private MstKeiroRepository mstKeiroRepository;    

    /**
     * マスタテーブルのデータ削除処理（登録経路編集画面）
     * 各行の削除機能
     * 
     * @param id 各行ID
     */
    public void deleteById(Long id) {
        mstKeiroRepository.deleteById(id);
    }

    /**
     * マスタテーブルのデータ削除処理（登録経路編集画面）
     * 一括削除機能
     * 
     * @param ids チェックされた行ID
     */
    public void deleteByAllIds(List<Long> ids) {
        mstKeiroRepository.deleteAllById(ids);
    }

}
