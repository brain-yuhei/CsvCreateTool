package com.example.csvcreate.model;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.Entity;
import lombok.Getter;
import lombok.Setter;

@Entity // このクラスがDBのテーブルと対応することを示す
@Getter // Lombokを使用して、すべてのフィールドのゲッターを自動生成
@Setter // Lombokを使用して、すべてのフィールドのセッターを自動生成
@Table(name = "MST_KEIRO")
public class MstKeiroEntity {

    @Id // idが主キーであることを示す
    @GeneratedValue(strategy = GenerationType.IDENTITY) // IDの自動生成
    private Long id; //ID
    private int detail_id; //明細番号
    private int related_id; //関連明細番号
    private LocalDate application_date; //申請日
    private String employee_id; //従業員番号
    private String employee_name; //従業員名
    private String payeeContent; //支払先・内容
    private String expense_category; //経費科目
    private BigDecimal converted_amount; //円換算金額
    private BigDecimal total_with_related; //関連明細との合計
    private BigDecimal total_tax_with_related; //関連明細との消費税額合計
    private String memo; //メモ
    private String inhouse_attendees; //自社出席者
    private Integer inhouse_attendees_count; //自社出席者人数
    private String external_attendees; //他社出席者
    private Integer external_attendees_count; //他社出席者人数
    private Integer total_attendees; //出席者数
    private BigDecimal amount_per_attendee; //出席者あたりの円換算金額
    private String department_code; //費用負担部門コード
    private String department_name; //費用負担部門名
    private String project_name; //プロジェクト名
    private String project_code; //プロジェクトコード
    private String tax_category; //税区分
    private String invoice_scheme; //インボイス経過措置
    private BigDecimal amountInclusiveTax; //金額（税込）
    private String currency; //通貨
    private BigDecimal exchange_rate; //為替レート
    private BigDecimal tax_amount; //消費税額
    private String debit_account; //借方勘定科目
    private String debit_sub_account; //借方補助科目
    private String credit_account; //貸方勘定科目
    private String credit_sub_account; //貸方補助科目
    private String source_of_entry; //明細取得元
    private Boolean journal_entry_created; //仕訳作成済
    private String pre_approval_number; //事前申請番号
    private String pre_approval_title; //事前申請タイトル
    private String expense_request_number; //経費申請番号
    private LocalDate request_date; //申請日
    private String request_status; //申請ステータス
    private String expense_request_title; //経費申請タイトル
    private String created_by_id; //作成者番号
    private String created_by_name; //作成者名
    private LocalDateTime created_at; //作成日時
    private String updated_by_id; //更新者番号
    private String updated_by_name; //更新者名
    private LocalDateTime updated_at; //更新日時
    private String aggregate_title; //集計タイトル
    private LocalDate date;
    
}
