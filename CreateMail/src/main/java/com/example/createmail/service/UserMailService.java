package com.example.createmail.service;

import com.example.createmail.model.UserMailEntity;
import com.example.createmail.repository.UserMailRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserMailService {

    @Autowired
    private UserMailRepository userMailRepository;

    /**
     * DBから送信済みメールを取得するメソッド
     * @return 送信済みメール
     */
    public List<UserMailEntity> getSentMails() {
        return userMailRepository.findBySent(true);
    }
 
    /**
     * DBから未送信メールを取得するメソッド
     * 
     * @return 未送信メール
     */
    public List<UserMailEntity> getUnsentMails() {
        return userMailRepository.findBySent(false);
    }
 
    /**
     * 送信済みと未送信の両方のメールを取得するメソッド
     * 
     * @return 送信・未送信メール
     */
    public List<UserMailEntity> getAllMails() {
        return userMailRepository.findAll(); 
    }

    /**
     * メールを送信済みに変更するメソッド
     * 
     * @param id メールID
     */
    public void markAsSent(Long id) {
        //メールIDに応じたメール情報を取得する
        UserMailEntity repository = userMailRepository.findById(id).orElseThrow();
        //未送信を送信済に変更する
        repository.setSent(true);
        //DBに変更を保存する
        userMailRepository.save(repository);
    }

    /**
     * 複数のIDに対応するメソッド
     * 
     * @param ids 複数のメールID
     */
    public void markAsSent(List<Long> ids) {      
        for (Long id : ids) {
            //ids内のidに対して送信済み処理を実行する
            markAsSent(id);
        }
    }

    /**
     * メールデータをDBに保存するメソッド
     * 
     * @param email メールアドレス
     * @param subject 件名
     * @param message 本文
     */
    public void saveRepository(String email, String subject, String message) {
        //DBのオブジェクト生成
        UserMailEntity repository = new UserMailEntity();
        repository.setEmail(email);
        repository.setSubject(subject);
        repository.setMessage(message);
        repository.setSent(false); //未送信でセット
        userMailRepository.save(repository);
    }
}



