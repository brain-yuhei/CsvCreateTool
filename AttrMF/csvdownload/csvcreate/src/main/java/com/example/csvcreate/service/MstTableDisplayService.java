package com.example.csvcreate.service;

import java.util.*;

import org.springframework.beans.factory.annotation.Autowired;

import com.example.csvcreate.model.MstKeiroEntity;
import com.example.csvcreate.repository.MstKeiroRepository;
import org.springframework.stereotype.Service;

@Service
public class MstTableDisplayService {

    @Autowired
    MstKeiroRepository mstKeiroRepository;

    /**
     * マスタテーブルから全データ取得処理
     * 
     * @return
     */
    public List<MstKeiroEntity> getMstDataForDisplay() {

        // マスタテーブルの全データを取得し返す
        return mstKeiroRepository.findByAll();
    }
}
