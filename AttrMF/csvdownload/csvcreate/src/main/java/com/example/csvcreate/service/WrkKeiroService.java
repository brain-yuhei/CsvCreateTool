package com.example.csvcreate.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.time.LocalDate;
import com.example.csvcreate.model.MstKeiroEntity;
import com.example.csvcreate.model.WrkKeiroEntity;
import com.example.csvcreate.repository.MstKeiroRepository;

@Service
public class WrkKeiroService {

    @Autowired
    private MstKeiroRepository mstKeiroRepository;

    public WrkKeiroEntity updateRowByPayee(String payee, LocalDate date) {
        List<MstKeiroEntity> matchedList = mstKeiroRepository.findByPayeeContent(payee);
    
        if (matchedList.isEmpty()) {
            return null;
        }
    
        MstKeiroEntity matched = matchedList.get(0); 
    
        WrkKeiroEntity wrk = new WrkKeiroEntity();
        wrk.setDate(date);
        wrk.setPayee(matched.getPayeeContent());
        wrk.setExpenseCategory(matched.getExpense_category());
        wrk.setAmount(matched.getAmountInclusiveTax());
        wrk.setMemo(matched.getMemo());
        wrk.setDepartmentName(matched.getDepartment_name());
        wrk.setDepartmentCode(matched.getDepartment_code());
    
        return wrk;
    }
    
}

