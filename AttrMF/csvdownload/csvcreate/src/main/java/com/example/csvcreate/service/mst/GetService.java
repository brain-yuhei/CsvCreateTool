package com.example.csvcreate.service.mst;

import java.util.stream.Collectors;
import java.util.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.csvcreate.model.MstKeiroEntity;
import com.example.csvcreate.repository.MstKeiroRepository;

@Service
public class GetService {
 
    @Autowired
    private MstKeiroRepository mstKeiroRepository;    

    /**
     * マスタテーブルのデータ一覧を取得
     * 
     * @return マスタテーブル内の全データ
     */
    public List<MstKeiroEntity> getMstList() {
        return mstKeiroRepository.findAll();
    }

    /**
     * 支払先・内容データの一覧を取得
     * 
     * @return 支払先・内容データ一覧
     */    
    public List<String> getSelectedPayees() {
        return mstKeiroRepository.findAll().stream()
                .map(MstKeiroEntity::getPayeeContent)
                .filter(payee -> payee != null && !payee.isEmpty())
                .distinct()
                .collect(Collectors.toList());
    }
    
    // /**
    //  * マスタテーブルから全データ取得処理
    //  * 
    //  * @return
    //  */
    // public List<MstKeiroEntity> getMstDataForDisplay() {

    //     // マスタテーブルの全データを取得し返す
    //     return mstKeiroRepository.findByAll();
    // }

}
