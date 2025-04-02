package com.example.createmail.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.createmail.model.UserMailEntity;

import java.util.List;

public interface UserMailRepository extends JpaRepository<UserMailEntity, Long> {
    // 送信済みのメールを取得するメソッド
    List<UserMailEntity> findBySent(boolean sent);
}
