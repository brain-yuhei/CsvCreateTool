package com.example.csvcreate.model;

import java.util.*;



public class CsvFormWrapperDto {
    
    private List<WrkKeiroEntity> koutsuuhiList;
    private String selectedMonth;
    private String selectedPayee; 

    public List<WrkKeiroEntity> getKoutsuuhiList() {
        return koutsuuhiList;
    }

    public void setKoutsuuhiList(List<WrkKeiroEntity> koutsuuhiList) {
        this.koutsuuhiList = koutsuuhiList;
    }

    public String getSelectedMonth() {
        return selectedMonth;
    }
    
    public void setSelectedMonth(String selectedMonth) {
        this.selectedMonth = selectedMonth;
    }
    
    public String getSelectedPayee() {
        return selectedPayee;
    }
    
    public void setSelectedPayee(String selectedPayee) {
        this.selectedPayee = selectedPayee;
    }

}
