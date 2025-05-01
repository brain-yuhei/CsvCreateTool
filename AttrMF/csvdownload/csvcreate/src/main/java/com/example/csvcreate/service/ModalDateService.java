package com.example.csvcreate.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.csvcreate.model.KoutsuuhiFormItem;
import com.example.csvcreate.repository.ModalDateRepository;

import java.util.*;

@Service
public class ModalDateService {

    @Autowired
    private ModalDateRepository modalDateRepository;

    public List<KoutsuuhiFormItem> findByDates(List<String> selectedDates){
        return modalDateRepository.findByDateIn(selectedDates);
    }
        
}

