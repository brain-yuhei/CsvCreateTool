package com.example.csvcreate.service.mst;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.csvcreate.repository.MstKeiroRepository;

@Service
public class DeleteService {
 
    @Autowired
    private MstKeiroRepository mstKeiroRepository;    

    /**
     * マスタテーブルのデータ削除処理（登録経路編集画面）
     * 
     * @param id
     */
    public void deleteById(Long id) {
        mstKeiroRepository.deleteById(id);
    }

}
