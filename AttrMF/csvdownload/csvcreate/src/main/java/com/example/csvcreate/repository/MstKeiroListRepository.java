package com.example.csvcreate.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.csvcreate.model.MstKeiroEntity;

public interface MstKeiroListRepository extends JpaRepository<MstKeiroEntity, Long>  {

        // 経路の一致するデータをすべて取得
    List<MstKeiroEntity> findByPayeeContent(String payeeContent); 

}