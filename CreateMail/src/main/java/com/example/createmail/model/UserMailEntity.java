package com.example.createmail.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

/**
 * ユーザーメールを表すエンティティクラス
 * DBの「user_mail_entity」テーブルに紐づきメールの送信先や送信状態を管理する
 */
@Entity // このクラスがDBのテーブルと対応することを示す
@Getter // Lombokを使用して、すべてのフィールドのゲッターを自動生成
@Setter // Lombokを使用して、すべてのフィールドのセッターを自動生成
public class UserMailEntity {

    @Id // idが主キーであることを示す
    @GeneratedValue(strategy = GenerationType.IDENTITY) // IDの自動生成

    private Long id; // ユーザーメールのID
    private String email; // メールアドレス
    private String subject; // 件名
    private String message; // 本文
    private boolean sent; // メールが送信済みかどうかのフラグ
    private Date sendDate; // 送信日時を表すフィールドを追加

}

