package com.example.csvcreate.model;

import java.util.*;

import lombok.Getter;
import lombok.Setter;

/**
 * CSV管理表の複数行を管理
 * 
 */
@Getter // Lombokを使用して、すべてのフィールドのゲッターを自動生成
@Setter // Lombokを使用して、すべてのフィールドのセッターを自動生成
public class KoutsuuhiFormWrapper {
    private List<KoutsuuhiFormItem> koutsuuhiList;
    private List<String> selectedDates;
}
