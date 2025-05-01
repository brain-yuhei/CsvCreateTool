package com.example.csvcreate.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.csvcreate.model.KoutsuuhiFormItem;

public interface ModalDateRepository extends JpaRepository<KoutsuuhiFormItem, Long>{

    List<KoutsuuhiFormItem> findByDateIn(List<String> dates);
    
}
